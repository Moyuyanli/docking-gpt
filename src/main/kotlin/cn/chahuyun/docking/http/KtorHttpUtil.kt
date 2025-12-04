package cn.chahuyun.docking.http

import cn.chahuyun.docking.BaseValue
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

/**
 * Ktor HTTP工具对象，用于创建和配置HTTP客户端
 *
 * 该对象封装了Ktor HttpClient的初始化配置，包括JSON序列化、超时设置和默认请求头配置。
 * 提供了一个预配置的HTTP客户端实例，可直接用于发送HTTP请求。
 */
object KtorHttpUtil {
    /**
     * 配置好的HttpClient实例
     *
     * 该客户端已预配置以下功能：
     * - JSON内容协商：自动处理JSON序列化和反序列化
     * - 超时设置：请求、连接和套接字超时均设置为30秒
     * - 默认请求配置：默认Content-Type为application/json
     */
    @OptIn(ExperimentalSerializationApi::class)
    val client = HttpClient(CIO) {
        /**
         * 安装内容协商插件，用于处理HTTP请求和响应的序列化
         * 配置为使用JSON格式进行数据传输
         */
        install(ContentNegotiation) {
            json(
                json = Json {
                    // 是否编码默认值，默认为false，设为true时会序列化默认值
                    encodeDefaults = true
                    // 是否宽松模式，允许解析非标准JSON格式，默认为false
                    isLenient = true
                    // 是否允许特殊浮点数值（如NaN、Infinity等），默认为false
                    allowSpecialFloatingPointValues = true
                    // 是否允许结构化Map键，即非字符串类型的键，默认为false
                    allowStructuredMapKeys = true
                    // 是否美化输出JSON格式，默认为false
                    prettyPrint = false
                    // 是否使用数组多态性，默认为false
                    useArrayPolymorphism = false
                    // 是否显式输出null值，
                    explicitNulls = false
                }
            )
        }

        /**
         * 配置HTTP超时设置
         * 统一设置请求超时、连接超时和套接字超时为30秒
         */
        install(HttpTimeout) {
            requestTimeoutMillis = 30.seconds.inWholeMilliseconds
            connectTimeoutMillis = 30.seconds.inWholeMilliseconds
            socketTimeoutMillis = 30.seconds.inWholeMilliseconds
        }

        /**
         * 配置默认请求参数
         * 设置所有请求的默认Content-Type为application/json
         */
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }

    /**
     * 发送POST请求
     *
     * @param url 请求的URL地址
     * @param body 请求体数据
     * @param token 认证令牌，默认使用HttpBaseValue.token
     */
    suspend fun post(url: String, body: Any, token: String = BaseValue.token): HttpResponse {
        // 执行POST请求，设置Bearer认证和请求体
        return client.post(url) {
            bearerAuth(token)
            setBody(body)
        }
    }

}
