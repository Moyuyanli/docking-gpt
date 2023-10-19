package cn.chahuyun.dockingGpt.match;

import cn.chahuyun.authorize.Interface.CustomPattern;
import cn.chahuyun.dockingGpt.PluginConfig;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;

import java.util.regex.Pattern;

/**
 * 自定义匹配
 *
 * @author Moyuyanli
 * @date 2023/8/9 10:31
 */
public class MessageMatch implements CustomPattern {
    /**
     * 一个自定义匹配规则
     *
     * @param event 事件
     * @return boolean
     * @author Moyuyanli
     * @date 2023/8/7 18:20
     */
    @Override
    public boolean custom(Event event) {
        MessageEvent messageEvent = event instanceof MessageEvent ? ((MessageEvent) event) : null;
        if (messageEvent == null) {
            return false;
        }
        System.out.println(PluginConfig.INSTANCE.getTrigger());
        String pattern = "^" + PluginConfig.INSTANCE.getTrigger() + ".*";
        return Pattern.matches(pattern, messageEvent.getMessage().contentToString());
    }
}
