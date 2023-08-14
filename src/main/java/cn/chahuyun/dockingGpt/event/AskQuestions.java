package cn.chahuyun.dockingGpt.event;

import cn.chahuyun.authorize.annotation.EventComponent;
import cn.chahuyun.authorize.annotation.MessageAuthorize;
import cn.chahuyun.authorize.enums.MessageMatchingEnum;
import cn.chahuyun.dockingGpt.docking.AbstractRequest;
import cn.chahuyun.dockingGpt.docking.RequestFactory;
import cn.chahuyun.dockingGpt.entity.MessageInfo;
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


    /**
     * 简单测试
     * @param event 消息事件
     */
    @MessageAuthorize(
            custom = MessageMatch.class,
            messageMatching = MessageMatchingEnum.CUSTOM,
            groupPermissions = "chat"
    )
    public void question(MessageEvent event) {
        MessageInfo messageInfo = new MessageInfo(event);
        RequestFactory requestFactory = RequestFactory.create();
        requestFactory.init();
        AbstractRequest openAiRequest = requestFactory.getOpenAiRequest();
        String result = openAiRequest.msgRequest(messageInfo);
        event.getSubject().sendMessage(result);
    }

}
