package cn.chahuyun.docking.entity

import jakarta.persistence.*
import java.time.LocalDateTime

/**
 *  请求message
 *
 * @author Moyuyanli
 * @date 2024/9/5 14:37
 */
@Entity
@Table(name = "question_message")
data class QuestionMessage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    /**
     * 群
     */
    @Column(name = "`group`")
    var group: Long? = null,
    /**
     * 发送人
     */
    var sender: Long? = null,
    /**
     * 引用回复id
     */
    var quoteId: Long? = null,
    /**
     * 提问消息id
     */
    var questionId: Long? = null,
    /**
     * 提问
     */
    var question: String? = null,
    /**
     * 提问时间
     */
    var timer: LocalDateTime? = null,
    /**
     * 回复消息id
     */
    var replyId: Long? = null,
    /**
     * 回答
     */
    var reply: String? = null,
    /**
     * 回答时间
     */
    var replyTime: LocalDateTime? = null,
)




