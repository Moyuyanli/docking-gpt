package cn.chahuyun.dockingGpt.entity;

import lombok.Data;
import net.mamoe.mirai.message.data.MessageSource;

/**
 * 消息信息
 *
 * @author Moyuyanli
 * @Date 2023/7/30 1:02
 */

@Data
public class MessageInfo {

    /**
     * 消息源
     */
    private MessageSource messageSource;
    /**
     * 发送人id
     */
    private long sendId;
    /**
     * 接受者id  群或好友
     */
    private long formId;


}
