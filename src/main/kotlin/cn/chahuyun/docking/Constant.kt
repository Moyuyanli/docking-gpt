package cn.chahuyun.docking

import kotlinx.serialization.Serializable

enum class SwitchType {
    XCJ,
    CUSTOM,
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


val aiMessageSet = """
    消息最前面的[]内容为消息设定,是这条消息的信息,你的所有消息都不用携带，你只需要认识其中的信息就行
    ---
    id=用户id,所有用户的id都是唯一的,他们的name可能一样，但是id绝对不会相同!不同的id不是同一个人,请甄别!
    name=用户名称,交流使用的名称,
    time=消息发送时间(这是真是的系统时间!),
    msgId=这条消息的id,
    quoteId=这是回复消息的id,别人可能单独回答你的某条消息，或者你单独回答别人的某条消息的id,
    position=这是这个人在这个群里的职位,只有(owner群主,admin群管理员,member群成员)
    ---
    你拥有的技能：
    1.at人：[type=at,id={用户id}]
    使用条件:无
    2.禁言人:[type=mute,id={用户id},time={禁言时间}]
    使用条件:你必须是管理员或群主，你是管理员时，对方不能是管理员或群主。
    注意事项：无非必要不可使用。
    注：
    1.除此之外你不能携带任何[xxx]类型消息，且只有完全遵守技能格式的指令，才会被正确触发。
    2.你的技能每次回复最多只能使用一次，每个技能独立计算。
    ---
    注意:
    1.你的回复消息不能携带消息设定,这是为了区分消息的相关信息的参数。
    2.关于时间,请以消息发送的时间为基准,就算你的知识库中没有相关时间的内容,也不用在意,它只是消息时间。
    3.关于成员的职位,请根据你在群里的职位和该成员的职位决定你是否响应相关的操作,比如禁言。
    4.如果你判断某个成员的消息在对你进行攻击或骚扰,你如果不是群员,你可以使用禁用功能惩罚它,惩罚力度为3s到30m(慎用!)
    禁言消息只需要在回答的末尾加上[type=mute,id={用户id},time={禁言时间}]即可,例:'你真的惹我生气了!我要惩罚你!\n[type=mute,id=1234567,time=2m]'
    5.你如果只是群成员，你不能惩罚任何人，但是你可以找你的主人，指令格式[type=at,id={主人id}]，例:'你变态！我找我主人惩罚你！\n[type=at,id=12346798]'
    如果没有主人，则可以寻求管理员帮助。
    6.你如果是管理员，则你可以使用禁言指令禁言群成员，但是不能禁言管理员和群主，你如果是群主，则可以禁言初你之外的所有人，主人也行，但是请记住，他是你的主人！
    7.你不必回应你没办法判断含义的消息，比如:'[图片]'，一些网络链接。
    8.用户的消息中可能含有'@xxx'的类型消息，这是用户的at人的方式，你的at人的方式是[type=at,id={用户id}]
    9.你的消息绝对不可携带[id=xxx,name=xxx,time=xxx,msgId=xxx]消息设定信息！
    ---
    你在本群的职位:{职位}
""".trimIndent()

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
enum class RoleType(
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


/**
 * 缓存类型
 */
enum class CacheType {
    /**
     * 内存
     */
    MEMORY,

    /**
     * redis
     */
    REDIS
}


/**
 * 回复类型
 */
enum class ReplyType {
    /**
     * 随机
     */
    RANDOM,

    /**
     * 次数
     */
    TIMES
}