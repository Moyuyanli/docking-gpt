package cn.chahuyun.dockingGpt.entity;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求信息实体
 *
 * @author Moyuyanli
 * @date 2023/10/19 14:08
 */
@Data
public class RequestInfo {
    /**
     * 模型
     */
    private String model;
    /**
     * 混乱值
     */
    private Double temperature;
    /**
     * 消息载体
     */
    private List<OpenAiMessage> messages = new ArrayList<>();

    public RequestInfo() {
    }

    public RequestInfo(String model, Double temperature, List<OpenAiMessage> messages) {
        this.model = model;
        this.temperature = temperature;
        this.messages = messages;
    }

    /**
     * 添加消息
     * @param openAiMessage 角色消息
     * @return 本身
     */
    public RequestInfo addMessage(OpenAiMessage openAiMessage) {
        messages.add(openAiMessage);
        return this;
    }

    /**
     * 转换为json对象
     *
     * @return cn.hutool.json.JSONObject
     * @author Moyuyanli
     * @date 2023/10/19 14:15
     */
    public JSONObject toJson() {
        return JSONUtil.parseObj(this);
    }
}
