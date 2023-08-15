package cn.chahuyun.dockingGpt.docking;

import cn.chahuyun.authorize.utils.Log;
import cn.chahuyun.dockingGpt.constant.Constant;
import cn.chahuyun.dockingGpt.constant.OpenAiMessageRoleEnum;
import cn.chahuyun.dockingGpt.entity.OpenAiMessage;
import cn.chahuyun.dockingGpt.entity.ProxyInfo;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;

/**
 * openai抽象接口
 *
 * @author Moyuyanli
 * @Date 2023/8/12 22:47
 */
@Data
public abstract class AbstractRequest implements OpenAiRequest {
    /**
     * 连接秘钥
     */
    private String aiKey;
    /**
     * ai设定
     */
    private String setAi;
    /**
     * 混乱值
     */
    private double temperature;
    /**
     * ai模型
     */
    private String model;
    /**
     * 代理信息
     */
    private ProxyInfo proxyInfo;

    public AbstractRequest(String aiKey, String setAi, double temperature, String model, ProxyInfo proxyInfo) {
        this.aiKey = aiKey;
        this.setAi = setAi;
        this.temperature = temperature;
        this.model = model;
        this.proxyInfo = proxyInfo;
    }

    /**
     * 获取消息构造
     * @return entries
     */
    public JSONObject getParams(String message) {
        JSONObject entries = JSONUtil.createObj();
        entries.set(Constant.AI_SET_MODEL, getModel());
        entries.set(Constant.AI_SET_TEMPERATURE, getTemperature());
        JSONArray array = JSONUtil.createArray();
        OpenAiMessage system = new OpenAiMessage(OpenAiMessageRoleEnum.SYSTEM, getSetAi());
        OpenAiMessage user = new OpenAiMessage(OpenAiMessageRoleEnum.USER, message);
        array.add(system);
        array.add(user);
        entries.set("messages", array);

        Log.debug("OpenAi request body ->" + entries);
        return entries;
    }

    /**
     * 发送请求
     * @return result 消息
     */
    public String execute(HttpRequest post) {
        try (HttpResponse httpResponse = post.execute()) {
            if (httpResponse.getStatus() == Constant.REQUEST_RESULT_STATUS_SUCCESS) {
                JSONObject response = JSONUtil.parseObj(httpResponse.body());
                JSONArray choices = response.getJSONArray("choices");
                JSONObject jsonObject = choices.getJSONObject(0);
                JSONObject message = jsonObject.getJSONObject("message");
                return message.getStr("content");
            } else {
                JSONObject response = JSONUtil.parseObj(httpResponse.body());
                return "请求错误,"+response.getStr("message");
            }
        } catch (Exception e) {
            return "请求错误,"+e.getMessage();
        }
    }

}
