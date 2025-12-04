package cn.chahuyun.docking.entity

import kotlinx.serialization.Serializable

/**
 * AI返回的消息内容
 * @property role 消息角色，例如："assistant"表示AI助手
 * @property content 消息文本内容
 * @property refusal 拒绝内容（可选，某些模型可能返回）
 */
@Serializable
data class Record(
    val role: String? = null,
    val content: String? = null,
    val refusal: String? = null,
)