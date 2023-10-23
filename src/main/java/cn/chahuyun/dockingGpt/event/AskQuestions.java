package cn.chahuyun.dockingGpt.event;

import cn.chahuyun.authorize.annotation.EventComponent;
import cn.chahuyun.authorize.annotation.MessageAuthorize;
import cn.chahuyun.authorize.enums.MessageMatchingEnum;
import cn.chahuyun.dockingGpt.docking.AbstractRequest;
import cn.chahuyun.dockingGpt.docking.RequestFactory;
import cn.chahuyun.dockingGpt.entity.RecordMessageInfo;
import cn.chahuyun.dockingGpt.match.MessageMatch;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.QuoteReply;

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

        RecordMessageInfo recordMessageInfo = new RecordMessageInfo(event);

        RequestFactory requestFactory = RequestFactory.create();

        AbstractRequest openAiRequest = requestFactory.getOpenAiRequest();
        String result = openAiRequest.msgRequest(recordMessageInfo);
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append(new QuoteReply(event.getMessage())).append(new PlainText(result));
        event.getSubject().sendMessage(builder.build());
    }

}
