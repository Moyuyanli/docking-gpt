package cn.chahuyun.dockingGpt.event;

import cn.chahuyun.authorize.annotation.EventComponent;
import cn.chahuyun.authorize.annotation.MessageAuthorize;
import cn.chahuyun.authorize.enums.MessageMatchingEnum;
import cn.chahuyun.dockingGpt.match.MessageMatch;
import net.mamoe.mirai.event.events.MessageEvent;

/**
 * 提问入口
 *
 * @author Moyuyanli
 * @Date 2023/7/30 0:50
 */
@EventComponent
public class AskQuestions {


    @MessageAuthorize(custom = MessageMatch.class, messageMatching = MessageMatchingEnum.CUSTOM)
    public void question(MessageEvent event) {
        event.getSubject().sendMessage("乌拉~");
    }

}
