@file:Suppress("PropertyName", "SpellCheckingInspection")

package cn.chahuyun.docking.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

/**
 * AI响应主类
 * @property id 响应唯一标识符，例如："chatcmpl-abc123"
 * @property object 对象类型，例如："chat.completion"
 * @property created 创建时间戳（Unix时间戳），例如：1677858242
 * @property model 使用的AI模型，例如："gpt-4o-mini"
 * @property usage 令牌使用情况统计
 * @property choices AI返回的消息选择列表
 */
@Serializable
data class AIResponse(
    val id: String? = null,
    val `object`: String? = null,
    val created: Long? = null,  // 时间戳建议使用Long类型
    val model: String? = null,
    val usage: Usage? = null,
    val choices: List<Choice> = emptyList(),
)

/**
 * 令牌使用情况统计
 * @property prompt_tokens 提示词消耗的令牌数
 * @property completion_tokens 完成文本消耗的令牌数
 * @property total_tokens 总消耗令牌数
 * @property completion_tokens_details 完成令牌的详细统计（可选）
 */
@Serializable
data class Usage(
    val prompt_tokens: Int? = null,  // 令牌数应该是数值类型
    val completion_tokens: Int? = null,
    val total_tokens: Int? = null,
    val completion_tokens_details: CompletionTokensDetails? = null,
)

/**
 * 完成令牌的详细统计信息
 * @property reasoning_tokens 推理过程消耗的令牌数
 * @property accepted_prediction_tokens 接受的预测令牌数
 * @property rejected_prediction_tokens 拒绝的预测令牌数
 */
@Serializable
data class CompletionTokensDetails(
    val reasoning_tokens: Int? = null,
    val accepted_prediction_tokens: Int? = null,
    val rejected_prediction_tokens: Int? = null,
)

/**
 * AI返回的消息选择项
 * @property index 选择项的索引，例如：0
 * @property message AI返回的消息内容
 * @property logprobs 对数概率信息，可能为null
 * @property finish_reason 完成原因，例如："stop"表示正常结束
 */
@Serializable
data class Choice(
    val index: Int? = null,
    val message: Record? = null,
    val logprobs: JsonElement = JsonNull,
    val finish_reason: String? = null,
)



