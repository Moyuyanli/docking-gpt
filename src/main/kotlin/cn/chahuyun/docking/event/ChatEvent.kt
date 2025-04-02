package cn.chahuyun.docking.event

import cn.chahuyun.authorize.EventComponent
import cn.chahuyun.authorize.MessageAuthorize
import cn.chahuyun.authorize.constant.AuthPerm
import cn.chahuyun.authorize.constant.MessageMatchingEnum
import cn.chahuyun.authorize.utils.MessageUtil.sendMessageQuery
import cn.chahuyun.docking.*
import cn.chahuyun.docking.entity.QuestionMessage
import cn.chahuyun.hibernateplus.HibernateFactory
import net.mamoe.mirai.contact.MemberPermission
import net.mamoe.mirai.contact.MemberPermission.*
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.source
import net.mamoe.mirai.message.sourceIds
import java.time.LocalDateTime

@EventComponent
class ChatEvent {

    /**
     * 小狐狸提问
     */
    @MessageAuthorize(
        custom = CustomMatch::class,
        messageMatching = MessageMatchingEnum.CUSTOM,
        groupPermissions = [PermCode.CHAT]
    )
    suspend fun chat(event: GroupMessageEvent) {
        val message = event.message
        val content = message.contentToString()
        message.source.ids

        val group = event.group
        val bot = event.bot
        val sender = event.sender

        val question = QuestionMessage(
            group = group.id,
            sender = sender.id,
            questionId = message.source.ids[0].toLong(),
            question = content,
            timer = LocalDateTime.now()
        )

        HibernateFactory.merge(question)


        val position = when (group.botPermission) {
            OWNER -> "owner"
            MEMBER -> "member"
            ADMINISTRATOR -> "admin"
        }
        val chat = ClientFactory.chat(question,position)


        val reply = event.sendMessageQuery(chat)

        MessageCache.cache(bot,group,reply)


        question.reply = chat
        question.replyId = reply.sourceIds[0].toLong()
        question.replyTime = LocalDateTime.now()

        HibernateFactory.merge(question)
    }

    @MessageAuthorize(
        ["切换模型 \\S+"],
        messageMatching = MessageMatchingEnum.REGULAR,
        userPermissions = [AuthPerm.OWNER, AuthPerm.ADMIN],
        groupPermissions = [PermCode.CHAT]
    )
    suspend fun switchModel(event: GroupMessageEvent) {
        val s = event.message.contentToString().split(" ")[1]
        event.sendMessageQuery(ClientFactory.switchModel(s))
    }

    @MessageAuthorize(
        ["当前模型"],
        userPermissions = [AuthPerm.OWNER, AuthPerm.ADMIN],
        groupPermissions = [PermCode.CHAT]
    )
    suspend fun viewModel(event: GroupMessageEvent) {
        event.sendMessageQuery(ClientFactory.viewModel())
    }

    @MessageAuthorize(
        ["模型列表", "拥有模型"],
        userPermissions = [AuthPerm.OWNER, AuthPerm.ADMIN],
        groupPermissions = [PermCode.CHAT]
    )
    suspend fun listModel(event: GroupMessageEvent) {
        event.sendMessageQuery(ClientFactory.listModel())
    }


    @MessageAuthorize(
        ["添加模型 \\S+ \\S+"],
        messageMatching = MessageMatchingEnum.REGULAR,
        userPermissions = [AuthPerm.OWNER, AuthPerm.ADMIN],
        groupPermissions = [PermCode.CHAT]
    )
    suspend fun addModel(event: GroupMessageEvent) {
        val split = event.message.contentToString().split(" ")
        event.sendMessageQuery(ClientFactory.addModel(split[1], split[2]))
    }

    /**
     * 解析输入字符串，返回类型名称、ID 和时间（以秒为单位）。
     *
     * @param input 输入字符串
     * @return Triple<String, Int, Int> 类型名称、ID 和时间（秒数）
     */
    private fun parseInput(input: String): Triple<String, Int, Int> {
        // 定义正则表达式：匹配 [type=(mute|at),id=数字(,time=数字[sm])] 格式
        val regex = """type=(mute|at),id=(\d+)(?:,time=(\d{0,2}[sm]))?""".toRegex()

        // 匹配输入字符串
        val matchResult = regex.matchEntire(input) ?: throw IllegalArgumentException("Invalid input format")

        // 提取类型名称
        val typeName = matchResult.groupValues[1]

        // 提取 ID
        val id = matchResult.groupValues[2].toInt()

        // 提取时间（如果存在），否则默认为 "0s"
        val timeString = matchResult.groupValues.getOrNull(3) ?: "0s"

        // 将时间转换为秒数
        val timeInSeconds = when {
            timeString.endsWith("s") -> timeString.dropLast(1).toInt() // 去掉 "s" 并转为整数
            timeString.endsWith("m") -> timeString.dropLast(1).toInt() * 60 // 去掉 "m" 并转为秒数
            else -> 0 // 默认值
        }

        return Triple(typeName, id, timeInSeconds)
    }

}