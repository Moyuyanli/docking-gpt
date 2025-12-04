package cn.chahuyun.docking.ai

import cn.chahuyun.docking.BaseValue
import cn.chahuyun.docking.BaseValue.model
import cn.chahuyun.docking.entity.AIResponse
import cn.chahuyun.docking.entity.Record
import cn.chahuyun.docking.entity.Usage
import cn.chahuyun.docking.http.KtorHttpUtil
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.serialization.SerializationException

/**
 * AI控制对象，负责处理AI聊天请求、模型切换和基础URL管理
 */
object AIControl {

    private val log = KotlinLogging.logger { }

    /**
     * 基础URL，从模型配置中获取默认渠道的URL，如果不存在则使用第一个非空URL并记录警告日志
     */
    var baseUrl = model.baseUrlMap[model.defBaseUrlAlias] ?: model.baseUrlMap.firstNotNullOf { it.value }.also {
        log.warn { "默认渠道下的BaseUrl不存在 已顺位到 $it" }
    }

    /**
     * 发起AI聊天请求
     * @param msg 聊天记录列表
     * @return AI响应结果，包含成功或失败的信息
     */
    suspend fun chat(msg: List<Record>): AIResult {
        // 参数验证
        if (msg.isEmpty()) {
            return AIResult.Error(
                type = ErrorType.VALIDATION_ERROR,
                message = "聊天记录不能为空"
            )
        }

        return try {
            val request = AIRequestFactory.createAiRequest(msg)
            val res = KtorHttpUtil.post(baseUrl, request)

            // 检查HTTP响应状态
            if (!res.status.isSuccess()) {
                return AIResult.Error(
                    type = ErrorType.HTTP_ERROR,
                    message = "HTTP请求失败: ${res.status}",
                    statusCode = res.status.value
                )
            }

            val body = res.body<AIResponse>()

            // 检查响应体结构
            if (body.choices.isEmpty()) {
                return AIResult.Error(
                    type = ErrorType.EMPTY_RESPONSE,
                    message = "AI返回的选择列表为空"
                )
            }

            // 安全地获取第一条有效消息
            val record = body.choices.firstOrNull { it.message?.content != null }?.message

            record?.content?.let { content ->
                AIResult.Success(
                    content = content,
                    usage = body.usage
                )
            } ?: AIResult.Error(
                type = ErrorType.EMPTY_RESPONSE,
                message = "AI返回的内容为空"
            )

        } catch (e: SerializationException) {
            AIResult.Error(
                type = ErrorType.PARSE_ERROR,
                message = "响应解析失败: ${e.message}"
            )
        } catch (e: ClientRequestException) {
            AIResult.Error(
                type = ErrorType.HTTP_ERROR,
                message = "客户端请求异常: ${e.message}",
                statusCode = e.response.status.value
            )
        } catch (e: ServerResponseException) {
            AIResult.Error(
                type = ErrorType.HTTP_ERROR,
                message = "服务器响应异常: ${e.message}",
                statusCode = e.response.status.value
            )
        } catch (e: Exception) {
            AIResult.Error(
                type = ErrorType.NETWORK_ERROR,
                message = "网络请求异常: ${e.message}"
            )
        }
    }

    /**
     * 更新默认AI模型
     * @param def 新的默认模型别名
     * @return 更新成功返回true，模型不存在返回false
     */
    fun updateDefModel(def: String): Boolean {
        model.modelMap[def] ?: return false
        model.defaultModelAlias = def
        BaseValue.getAIModel()
        return true
    }

    /**
     * 更新默认基础URL
     * @param def 新的基础URL别名
     * @return 更新成功返回true，URL不存在返回false
     */
    fun updateDefBaseUrl(def: String): Boolean {
        baseUrl = model.baseUrlMap[def] ?: return false
        return true
    }

}

/**
 * AI响应结果密封类
 */
sealed class AIResult {
    /**
     * 成功响应
     * @property content AI返回的内容
     * @property usage 令牌使用情况（可选）
     */
    data class Success(
        val content: String,
        val usage: Usage? = null
    ) : AIResult()

    /**
     * 失败响应
     * @property type 错误类型
     * @property message 错误信息
     * @property statusCode HTTP状态码（可选）
     */
    data class Error(
        val type: ErrorType,
        val message: String,
        val statusCode: Int? = null
    ) : AIResult()
}

/**
 * AI错误类型枚举
 * 定义了系统中可能遇到的各种错误类型，用于统一错误处理和分类
 */
enum class ErrorType {
    /**
     * 网络错误
     * 包括连接超时、DNS解析失败、网络不可达等网络层面的错误
     */
    NETWORK_ERROR,

    /**
     * HTTP错误（非2xx）
     * 包括4xx客户端错误和5xx服务器错误等HTTP状态码异常
     */
    HTTP_ERROR,

    /**
     * 解析错误
     * 包括JSON解析失败、响应格式不正确等数据解析相关的错误
     */
    PARSE_ERROR,

    /**
     * 空响应
     * 包括响应体为空、关键字段缺失等返回数据不完整的情况
     */
    EMPTY_RESPONSE,

    /**
     * API业务错误
     * 包括API返回的业务逻辑错误，如配额不足、权限不足等
     */
    API_ERROR,

    /**
     * 参数验证错误
     * 包括输入参数不符合要求、必填参数缺失等前置校验失败的情况
     */
    VALIDATION_ERROR
}

