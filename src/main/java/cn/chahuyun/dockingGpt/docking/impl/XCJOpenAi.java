package cn.chahuyun.dockingGpt.docking.impl;

import cn.chahuyun.authorize.utils.Log;
import cn.chahuyun.dockingGpt.constant.Constant;
import cn.chahuyun.dockingGpt.constant.OpenAiMessageRoleEnum;
import cn.chahuyun.dockingGpt.docking.AbstractRequest;
import cn.chahuyun.dockingGpt.entity.MessageInfo;
import cn.chahuyun.dockingGpt.entity.OpenAiMessage;
import cn.chahuyun.dockingGpt.entity.ProxyInfo;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 使用小纯洁openAi地址
 *
 * @author Moyuyanli
 * @Date 2023/7/30 1:13
 */
public class XCJOpenAi extends AbstractRequest {


    public XCJOpenAi(String aiKey, String setAi, double temperature, String model, ProxyInfo proxyInfo) {
        super(aiKey, setAi, temperature, model, proxyInfo);
    }

    /**
     * 暂时的一个可以实现功能的实现方法
     * @param info 消息信息
     * @return
     */
    @Override
    public String msgRequest(MessageInfo info) {
        JSONObject entries = JSONUtil.createObj();
        entries.set(Constant.AI_SET_MODEL, getModel());
        entries.set(Constant.AI_SET_TEMPERATURE, getTemperature());
        JSONArray array = JSONUtil.createArray();
        OpenAiMessage system = new OpenAiMessage(OpenAiMessageRoleEnum.SYSTEM, getSetAi());
        OpenAiMessage user = new OpenAiMessage(OpenAiMessageRoleEnum.USER, info.getMessage());
        array.add(system);
        array.add(user);
        entries.set("messages", array);

        Log.debug("OpenAi request body ->" + entries);

        HttpRequest post = HttpUtil.createPost(Constant.XCJ_AI_URL);
        post.contentType(Constant.REQUEST_HEAD_TYPE);
        post.auth("Bearer " + getAiKey());

        post.body(entries.toString());

        try (HttpResponse httpResponse = post.execute()) {
            if (httpResponse.getStatus() == 200) {
                JSONObject response = JSONUtil.parseObj(httpResponse.body());
                JSONArray choices = response.getJSONArray("choices");
                JSONObject jsonObject = choices.getJSONObject(0);
                JSONObject message = jsonObject.getJSONObject("message");
                return message.getStr("content");
            }
            return "请求错误！";
        } catch (Exception e) {
            return "请求错误！";
        }
    }
}
