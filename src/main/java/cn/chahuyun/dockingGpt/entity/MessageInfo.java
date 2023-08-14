package cn.chahuyun.dockingGpt.entity;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;

import java.io.Serializable;

/**
 * 消息信息
 *
 * @author Moyuyanli
 * @Date 2023/7/30 1:02
 */

@Data
public class MessageInfo implements Serializable {


    /**
     * 消息源
     */
    private MessageSource messageSource;
    /**
     * 消息源json化
     */
    private String sourceJson;
    /**
     * 消息内容
     */
    private MessageChain messageChain;
    /**
     * 消息发送时间戳(秒)
     */
    private int sendTime;
    /**
     * 机器人id
     */
    private long botId;
    /**
     * 发送人id
     */
    private long sendId;
    /**
     * 接受者id  群或好友
     */
    private long formId;
    /**
     * 消息内容
     * <li> 如果为提问消息(第一次询问)则直接换为 String
     * <li> 如果为复问消息(回复上提问)则应去掉 At 后转换 String
     */
    private String message;
    /**
     * 提问类型：true 问答 false 对话
     */
    private boolean questionType;
    /**
     * 用于对接 壶言经济的 计费数<p>
     * 1个字符=1金币
     */
    private int billing;

    /**
     * 新建消息信息
     *
     * @param event 消息事件
     */
    public MessageInfo(MessageEvent event) {
        this.messageSource = event.getSource();
        this.formId = messageSource.getFromId();
        this.sendId = messageSource.getTargetId();
        this.sendTime = messageSource.getTime();
        this.botId = messageSource.getBotId();

        MessageChain messages = event.getMessage();
        this.messageChain = messages;
        this.sourceJson = MessageChain.serializeToJsonString(messages);

        // 去掉at消息
        MessageChain tempChain = MessageChain.deserializeFromJsonString(sourceJson);
        for (SingleMessage singleMessage : tempChain) {
            if (singleMessage instanceof At) {
                messages.remove(singleMessage);
            }
        }

        if (messages.contains(QuoteReply.Key)) {
            this.questionType = false;
        }

        String contentToString = messages.contentToString();
        this.message = contentToString;
        this.billing = contentToString.length();
        this.sourceJson = MessageChain.serializeToJsonString(messages);
    }
}
