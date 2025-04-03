package cn.chahuyun.docking.event

import cn.chahuyun.authorize.EventComponent
import cn.chahuyun.authorize.MessageAuthorize
import cn.chahuyun.authorize.constant.AuthPerm
import cn.chahuyun.authorize.constant.MessageMatchingEnum
import cn.chahuyun.authorize.utils.MessageUtil.sendMessageQuery
import cn.chahuyun.docking.ClientFactory
import cn.chahuyun.docking.CustomMatch
import cn.chahuyun.docking.Docking.log
import cn.chahuyun.docking.MessageCache
import cn.chahuyun.docking.PermCode
import cn.chahuyun.docking.entity.QuestionMessage
import cn.chahuyun.hibernateplus.HibernateFactory
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.MemberPermission.*
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.*
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
        val chat = ClientFactory.chat(question, position)

        val reply = handleReplyMessage(event, chat)[0]

        MessageCache.cache(bot, group, reply)

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


    /**
     * 对指令进行识别
     */
    private suspend fun handleReplyMessage(event: GroupMessageEvent, reply: String): List<MessageReceipt<Contact>> {
        log.debug("回复处理消息->$reply")

        val botId = Bot.instances[0].id

        // 分割消息为多行
        val split = reply.split("\n")
        val messageReceipts = mutableListOf<MessageReceipt<Contact>>() // 存储每条消息的发送结果

        val mutePairs = mutableListOf<Pair<Long, String>>() // 用于收集 mute 类型的 Pair

        for (line in split) {
            if (line.contains("id=$botId")) {
                log.debug("检测到bot自己携带消息信息,以忽略! $line")
                continue
            }

            val regex = """\[type=(mute|at),id=(\d+)(?:,time=(\d{1,2}[smh]))?]""".toRegex()
            val resultMessages = mutableListOf<Any>() // 用于存储当前行的消息组件

            var remainingText = line // 当前行的剩余未处理文本
            var matchResult = regex.find(remainingText)

            while (matchResult != null) {
                val (type, id, time) = matchResult.destructured

                // 获取匹配前的文本（即普通文本部分）
                val prefixText = remainingText.substring(0, matchResult.range.first)
                if (prefixText.isNotEmpty()) {
                    resultMessages.add(PlainText(prefixText)) // 将普通文本添加到结果中
                }

                when (type) {
                    "at" -> {
                        // 对于 at 类型，转换为 PlainText + At(id) + PlainText
                        resultMessages.add(At(id.toLong())) // 添加 At 组件
                    }

                    "mute" -> {
                        // 对于 mute 类型，收集到 mutePairs 中
                        mutePairs.add(Pair(id.toLong(), time))
                    }
                }

                // 更新剩余文本（去掉已处理的部分）
                remainingText = remainingText.substring(matchResult.range.last + 1)
                matchResult = regex.find(remainingText)
            }

            // 如果还有剩余文本，添加到最后
            if (remainingText.isNotEmpty()) {
                resultMessages.add(PlainText(remainingText))
            }

            // 构建当前行的消息链
            val finalMessageChain = buildMessageChain(resultMessages)

            // 发送当前行的消息
            val receipt = event.subject.sendMessage(finalMessageChain)
            messageReceipts.add(receipt)
        }

        if (event.group.botPermission != MEMBER) {
            for (pair in mutePairs) {
                val id = pair.first
                val second = pair.second

                val c = second[second.length - 1]
                val cardinal = second.substring(0, second.length - 1).toInt()
                var result = 0
                when (c) {
                    's' -> result = cardinal * 1
                    'm' -> result = cardinal * 60
                }

                event.group[id]?.mute(result)
            }
        }
        return messageReceipts
    }

    private fun buildMessageChain(messages: List<Any>): MessageChain {
        var at =false
        val messageChainBuilder = MessageChainBuilder()
        messages.forEach { message ->
            when (message) {
                is PlainText -> messageChainBuilder.append(message)
                is At -> messageChainBuilder.append(message)
                else -> throw IllegalArgumentException("Unsupported message type: ${message::class.simpleName}")
            }
        }
        return messageChainBuilder.build()
    }

}