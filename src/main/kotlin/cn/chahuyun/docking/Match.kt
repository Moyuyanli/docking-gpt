package cn.chahuyun.docking

import cn.chahuyun.authorize.match.CustomPattern
import cn.chahuyun.docking.config.ForbiddenWords
import cn.chahuyun.docking.config.PluginConfig
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.MessageEvent


class CustomMatch : CustomPattern {
    /**
     * 一个自定义匹配规则
     *
     * @param event 事件
     * @return boolean
     * @author Moyuyanli
     * @date 2023/8/7 18:20
     */
    override fun custom(event: Event): Boolean {
        event as MessageEvent
        val content = event.message.contentToString()

        val forbidden = ForbiddenWords.forbidden

        for (s in forbidden) {
            val regex = Regex.fromLiteral(s)

            if (regex.find(content) != null) {
                return false
            }

        }

        return Regex("^${PluginConfig.trigger}.*").matches(content)
    }
}