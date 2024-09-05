package cn.chahuyun.dockingGpt.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

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
    var id: Int? = null,
    var group: Long? = null,
    var sender: Long? = null,
    var question: String? = null,
    var timer: Date? = null,
    var reply: String? = null,
    var replyTime: Date? = null,
)
