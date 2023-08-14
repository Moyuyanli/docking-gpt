package cn.chahuyun.dockingGpt.constant;

/**
 * ai消息角色
 *
 * @author Moyuyanli
 * @date 2023/8/14 14:45
 */
public enum OpenAiMessageRoleEnum {
    /**
     * 角色—系统
     */
    SYSTEM("system"),
    /**
     * 角色-用户
     */
    USER("user"),
    /**
     * 角色-ai
     */
    ROLE("role");
    /**
     * 值
     */
    private String value;

    OpenAiMessageRoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
