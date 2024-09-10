package cn.chahuyun.docking

import kotlinx.serialization.Serializable

enum class SwitchType {
    XCJ,
    OFFICIAL
}

@Serializable
enum class ProxyKey {
    Type, URL, Port
}

object PermCode {

    /**
     * 聊天
     */
    const val CHAT = "chat"


}

object RequestParams {
    /**
     * 提问请求后缀
     */
    const val REQUEST_URL_SUFFIX: String = "v1/chat/completions"

    /**
     * http请求-参数类型
     */
    const val REQUEST_HEAD_TYPE: String = "application/json"
}


/**
 * ai消息角色
 *
 * @author Moyuyanli
 * @date 2023/8/14 14:45
 */
enum class RoleEnum(
    /**
     * 值
     */
    var value: String,
) {
    /**
     * 角色—系统
     */
    SYSTEM("system"),

    /**
     * 角色-用户
     */
    USER("user"),

    /**
     * 角色-ai
     */
    ROLE("assistant")
}