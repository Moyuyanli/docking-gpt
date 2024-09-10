package cn.chahuyun.docking.event

import cn.chahuyun.authorize.EventComponent
import cn.chahuyun.authorize.MessageAuthorize
import cn.chahuyun.authorize.constant.MessageMatchingEnum
import cn.chahuyun.authorize.utils.MessageUtil.sendMessageQuery
import cn.chahuyun.docking.CustomMatch
import cn.chahuyun.docking.PermCode
import cn.chahuyun.docking.entity.QuestionMessage
import cn.chahuyun.docking.ChatFactory
import cn.chahuyun.hibernateplus.HibernateFactory
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

        val question = QuestionMessage(
            group = event.group.id,
            sender = event.sender.id,
            questionId = message.source.ids[0].toLong(),
            question = content,
            timer = LocalDateTime.now()
        )

        HibernateFactory.merge(question)

        val chat = ChatFactory.chat(question)

        val reply = event.sendMessageQuery(chat)

        question.reply = chat
        question.replyId = reply.sourceIds[0].toLong()
        question.replyTime = LocalDateTime.now()

        HibernateFactory.merge(question)
    }

}