package cn.chahuyun.docking.entity

import cn.hutool.json.JSONObject
import cn.hutool.json.JSONUtil
import lombok.Data

/**
 * 请求结果实体
 *
 * @author Moyuyanli
 * @date 2023/10/19 14:37
 */
@Data
data class ResponseInfo(
    var id: String? = null,
    var `object`: String? = null,
    var created: Int? = null,
    var model: String? = null,
    var usage: Usage? = null,
    var choices: List<ResultMessage> = ArrayList(),
) {

    val message: String
        /**
         * 获取回答(第一条消息)
         * @return 回答
         */
        get() = getMessage(0)

    /**
     * 获取对应下标的结果信息
     * @param index 下标
     * @return 回答
     */
    fun getMessage(index: Int): String {
        if (choices.isEmpty()) {
            return "错误:返回结果为空！"
        }
        return choices[index].message?.content ?: "错误:返回结果为空！"
    }

    /**
     * 转换为json对象
     *
     * @return cn.hutool.json.JSONObject
     * @author Moyuyanli
     * @date 2023/10/19 14:15
     */
    fun toJson(): JSONObject {
        return JSONUtil.parseObj(this)
    }
}


/**
 * 请求信息实体
 *
 * @author Moyuyanli
 * @date 2023/10/19 14:08
 */
@Data
class RequestInfo(
    /**
     * 模型
     */
    private var model: String?,
    /**
     * 混乱值
     */
    private var temperature: Double?,
    /**
     * 消息载体
     */
    private var messages: MutableList<Record>,
) {

    /**
     * 添加消息
     * @param openAiMessage 角色消息
     * @return 本身
     */
    fun addMessage(openAiMessage: Record): RequestInfo {
        messages.add(openAiMessage)
        return this
    }

    /**
     * 转换为json对象
     *
     * @return cn.hutool.json.JSONObject
     * @author Moyuyanli
     * @date 2023/10/19 14:15
     */
    fun toJson(): JSONObject {
        return JSONUtil.parseObj(this)
    }
}

@Data
class Usage(
    val prompt_tokens: String? = null,
    val completion_tokens: String? = null,
    val total_tokens: String? = null,
)

@Data
class ResultMessage(
    val index: Int? = null,
    val finish_reason: String? = null,
    val message: Record? = null,
)

