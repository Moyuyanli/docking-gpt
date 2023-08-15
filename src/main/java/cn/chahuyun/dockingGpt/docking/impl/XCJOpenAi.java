package cn.chahuyun.dockingGpt.docking.impl;

import cn.chahuyun.dockingGpt.constant.Constant;
import cn.chahuyun.dockingGpt.docking.AbstractRequest;
import cn.chahuyun.dockingGpt.entity.MessageInfo;
import cn.chahuyun.dockingGpt.entity.ProxyInfo;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;

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
     *
     * @param info 消息信息
     * @return
     */
    @Override
    public String msgRequest(MessageInfo info) {
        JSONObject entries = getParams(info.getMessage());

        HttpRequest post = HttpUtil.createPost(Constant.XCJ_AI_URL);
        post.contentType(Constant.REQUEST_HEAD_TYPE);
        post.auth("Bearer " + getAiKey());

        post.body(entries.toString());

        return execute(post);
    }
}
