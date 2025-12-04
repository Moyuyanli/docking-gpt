package cn.chahuyun.docking.entity

import cn.chahuyun.docking.BaseValue
import kotlinx.serialization.Serializable
import java.util.Collections.emptyList

@Serializable
data class AIRequest(
    /**
     * 模型
     */
     val model: String = BaseValue.getAIModel(),
    /**
     * 混乱值
     */
     val temperature: Double = BaseValue.temperature,
    /**
     * 消息载体
     */
     val messages: List<Record> = emptyList(),
)
