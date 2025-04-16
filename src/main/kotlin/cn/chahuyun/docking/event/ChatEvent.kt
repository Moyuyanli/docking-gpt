package cn.chahuyun.docking.event

import cn.chahuyun.authorize.EventComponent
import cn.chahuyun.authorize.MessageAuthorize
import cn.chahuyun.authorize.constant.AuthPerm
import cn.chahuyun.authorize.constant.MessageMatchingEnum
import cn.chahuyun.authorize.utils.MessageUtil.sendMessageQuery
import cn.chahuyun.docking.*
import cn.chahuyun.docking.Docking.log
import cn.chahuyun.docking.config.PluginConfig
import cn.chahuyun.docking.entity.QuestionMessage
import cn.chahuyun.hibernateplus.HibernateFactory
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.MemberPermission.*
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.sourceIds
import java.time.LocalDateTime
import kotlin.random.Random

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
        val group = event.group

        if (CustomMatch.isReplyStatus(group)) {
            return
        }

        CustomMatch.openReplyStatus(group)

        val message = event.message
        val content = message.contentToString()
        message.source.ids

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

        val aiSet = aiMessageSet.replace("{职位}", position)
            .replace("{禁言权限}", PluginConfig.notMutePowerGroups.contains(group.id).not().toString())

        val chat = ClientFactory.chat(question, aiSet)

        val replyMessage = handleReplyMessage(event, chat)
        if (replyMessage.isEmpty()) {
            return
        }

        val reply = replyMessage[0]

        question.reply = chat
        question.replyId = reply.sourceIds[0].toLong()
        question.replyTime = LocalDateTime.now()

        HibernateFactory.merge(question)

        CustomMatch.closeReplyStatus(group)
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


    @MessageAuthorize(
        ["启用随机回复", "开启随机回复"],
        messageMatching = MessageMatchingEnum.TEXT,
        userPermissions = [AuthPerm.OWNER, AuthPerm.ADMIN],
        groupPermissions = [PermCode.CHAT]
    )
    suspend fun addRandom(event: GroupMessageEvent) {
        val response = PluginConfig.groupRandomResponse
        val groupId = event.group.id
        if (!response.contains(groupId)) {
            response.add(groupId)
            sendMessageQuery(event, "已启用本群随机回复响应")
        }
    }

    @MessageAuthorize(
        ["关闭随机回复"],
        messageMatching = MessageMatchingEnum.TEXT,
        userPermissions = [AuthPerm.OWNER, AuthPerm.ADMIN],
        groupPermissions = [PermCode.CHAT]
    )
    suspend fun delRandom(event: GroupMessageEvent) {
        val response = PluginConfig.groupRandomResponse
        val groupId = event.group.id
        if (response.contains(groupId)) {
            response.remove(groupId)
            sendMessageQuery(event, "已关闭本群随机回复响应")
        }
    }

    @MessageAuthorize(
        ["启用禁言", "开启禁言"],
        messageMatching = MessageMatchingEnum.TEXT,
        userPermissions = [AuthPerm.OWNER, AuthPerm.ADMIN],
        groupPermissions = [PermCode.CHAT]
    )
    suspend fun addMute(event: GroupMessageEvent) {
        val response = PluginConfig.notMutePowerGroups
        val groupId = event.group.id
        if (response.contains(groupId)) {
            response.remove(groupId)
            sendMessageQuery(event, "已开启本群禁言权限")
        }
    }

    @MessageAuthorize(
        ["关闭禁言"],
        messageMatching = MessageMatchingEnum.TEXT,
        userPermissions = [AuthPerm.OWNER, AuthPerm.ADMIN],
        groupPermissions = [PermCode.CHAT]
    )
    suspend fun delMute(event: GroupMessageEvent) {
        val response = PluginConfig.notMutePowerGroups
        val groupId = event.group.id
        if (!response.contains(groupId)) {
            response.add(groupId)
            sendMessageQuery(event, "以关闭本群禁言权限")
        }
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

        val bot = event.bot
        for (line in split) {
            if (line.contains("id=$botId")) {
                log.debug("检测到bot自己携带消息信息,以忽略! $line")
                continue
            }
            log.debug("单行消息->$line")
            val regex = """\[type=(mute|at),id=(\d+)(?:,time=(\d{1,2}[smh]))?]""".toRegex()
            val resultMessages = mutableListOf<Any>() // 用于存储当前行的消息组件

            // 当前行的剩余未处理文本
            var remainingText = line.replace("\n", "").replace("\\n", "")
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
            val string = finalMessageChain.contentToString()
            if (string.isBlank() || string.contains("system") || string.isEmpty()) {
                continue
            }
            log.debug("回复消息->$string")

            // 发送当前行的消息
            val receipt = event.subject.sendMessage(finalMessageChain)
            MessageCache.cache(
                MessageCacheEntity(
                    event.group,
                    bot,
                    receipt.source.ids[0],
                    MessageCache.getNowTime(),
                    string,
                    MessageActionType.MSG
                )
            )
            messageReceipts.add(receipt)

            val (min, max) = PluginConfig.delayReplyTime
            randomDelay(min, max)
        }

        if (event.group.botPermission != MEMBER && PluginConfig.notMutePowerGroups.contains(event.group.id).not()) {
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

                val member = event.group[id]
                member?.mute(if (result > 1800) 1800 else result)
                MessageCache.cache(
                    MessageCacheEntity(
                        event.group,
                        bot,
                        -1,
                        MessageCache.getNowTime(),
                        "[type=mute,id=$id,time=$second]\n" +
                                "system:[id=${bot.id},name=\"${bot.nameCardOrNick}\"]" +
                                "对[id=$id,name=\"${member?.nameCardOrNick ?: "未知用户名"}\"]使用了禁言，时间[time=$second]",
                        MessageActionType.MUTE
                    )
                )
            }
        }
        return messageReceipts
    }

    private fun buildMessageChain(messages: List<Any>): MessageChain {
        val messageChainBuilder = MessageChainBuilder()
        messages.forEach { message ->
            when (message) {
                is PlainText -> {
                    val replace = message.content.replace(Regex("\\[.*?]"), "")
                    messageChainBuilder.append(PlainText(replace))
                }

                is At -> messageChainBuilder.append(message)
                else -> throw IllegalArgumentException("Unsupported message type: ${message::class.simpleName}")
            }
        }
        return messageChainBuilder.build()
    }


    /**
     * 根据配置的最小值和最大值进行随机延迟。
     *
     * @param min 最小延迟时间（秒）
     * @param max 最大延迟时间（秒）
     */
    private fun randomDelay(min: Int, max: Int) {
        // 修复非法参数：如果 min < 0 或 max < 0 或 min > max，则设置为双 0
        val adjustedMin = if (min < 0 || max < 0 || min > max) 0 else min
        val adjustedMax = if (min < 0 || max < 0 || min > max) 0 else max

        // 将秒转换为毫秒
        val delayTime = Random.nextInt(adjustedMin * 1000, (adjustedMax + 1) * 1000) // 生成 [min*1000, max*1000] 范围内的随机数
        println("随机延迟时间: ${delayTime / 1000} 秒")
        Thread.sleep(delayTime.toLong()) // 延迟指定的毫秒数
    }
}