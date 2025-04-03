package cn.chahuyun.docking

import cn.chahuyun.authorize.match.CustomPattern
import cn.chahuyun.docking.config.PluginConfig
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random


class CustomMatch : CustomPattern {
    companion object {
        /**
         * 记录器：记录每个群的状态、时间戳和触发次数
         */
        private val recorder: MutableMap<Long, Triple<Boolean, Instant, Int>> = ConcurrentHashMap()
    }

    /**
     * 自定义匹配规则
     *
     * @param event 事件
     * @return boolean
     */
    override fun custom(event: Event): Boolean {
        // 确保事件是 GroupMessageEvent
        if (event !is GroupMessageEvent) return false

        //记录消息
        MessageCache.cache(event)

        val group = event.group
        val groupId = group.id
        val message = event.message
        val content = message.contentToString()

        // 获取当前群的状态（是否激活）、激活时间和触发次数
        var (isActive, activationTime, triggerCount) =
            recorder.getOrDefault(groupId, Triple(false, Instant.now(), 0))

        // 如果超过3分钟，关闭状态
        if (isActive && Instant.now().isAfter(activationTime.plusSeconds(180))) {
            recorder[groupId] = Triple(false, Instant.now(), 0)
            isActive = false
        }

        // 更新触发次数
        recorder[groupId] = Triple(isActive, activationTime, triggerCount + 1)

        // 如果触发次数超过 10 次，关闭群状态
        if (triggerCount > 10) {
            recorder[groupId] = Triple(false, Instant.now(), 0)
            isActive = false
            triggerCount = 0
        }

        // 检查是否命中触发词
        if (Regex("^${PluginConfig.trigger}.*").matches(content)) {
            refreshRecorder(groupId) // 刷新记录器
            return true
        }

        // 检查是否@了机器人
        message.forEach {
            if (it is At && it.target == Bot.instances[0].id) {
                refreshRecorder(groupId) // 刷新记录器
                return true
            }
        }

        //未启用随机响应和状态
        if (!PluginConfig.groupRandomResponse.contains(groupId)) {
            return false
        }

        // 如果群未激活，使用 notActivatedProbability 判断
        if (!isActive) {
            val randomValue = Random.nextInt(100)
            return randomValue < PluginConfig.notActivatedProbability
        }

        // 如果群已激活，根据 activatedReplyMode 进行判断
        when (PluginConfig.activatedReplyMode.first) {
            ReplyType.RANDOM -> {
                val randomValue = Random.nextInt(100)
                return randomValue < PluginConfig.activatedReplyMode.second
            }

            ReplyType.TIMES -> {
                // 如果触发次数达到配置的次数，返回 true
                if (triggerCount == PluginConfig.activatedReplyMode.second) {
                    return true
                }
            }
        }

        return false
    }

    /**
     * 刷新记录器
     *
     * @param groupId 群 ID
     */
    private fun refreshRecorder(groupId: Long) {
        recorder[groupId] = Triple(true, Instant.now(), 0)
    }
}