package cn.chahuyun.dockingGpt.entity;

import cn.chahuyun.dockingGpt.constant.OpenAiMessageRoleEnum;
import lombok.Data;

@Data
public class OpenAiMessage {
    /**
     * 消息角色
     */
    private String role;
    /**
     * 用户名
     */
    private String name;
    /**
     * 内容
     */
    private String content;

    public OpenAiMessage(OpenAiMessageRoleEnum role, String content) {
        this.role = role.getValue();
        this.content = content;
    }

    public OpenAiMessage(OpenAiMessageRoleEnum role, String name, String content) {
        this.role = role.getValue();
        this.name = name;
        this.content = content;
    }

}
