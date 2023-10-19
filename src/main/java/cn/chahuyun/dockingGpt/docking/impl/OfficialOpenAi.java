package cn.chahuyun.dockingGpt.docking.impl;

import cn.chahuyun.dockingGpt.constant.Constant;
import cn.chahuyun.dockingGpt.docking.AbstractRequest;
import cn.chahuyun.dockingGpt.entity.RecordMessageInfo;
import cn.chahuyun.dockingGpt.entity.ProxyInfo;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;

/**
 * 使用官方openAi地址
 *
 * @author Moyuyanli
 * @Date 2023/7/30 1:15
 */
public class OfficialOpenAi extends AbstractRequest {


    public OfficialOpenAi(String aiKey, String setAi, double temperature, String model, ProxyInfo proxyInfo) {
        super(aiKey, setAi, temperature, model, proxyInfo);
    }

    /**
     * 暂时的一个可以实现功能的实现方法
     *
     * @param info 消息信息
     * @return
     */
    @Override
    public String msgRequest(RecordMessageInfo info) {
        JSONObject entries = getParams(info.getMessage()).toJson();

        HttpRequest post = HttpUtil.createPost(Constant.OFFICIAL_AI_URL);
        post.contentType(Constant.REQUEST_HEAD_TYPE);
        post.auth("Bearer " + getAiKey());

        if (getProxyInfo().getProxyType().equals(Constant.HTTP)) {
            post.setHttpProxy(getProxyInfo().getUrl(), getProxyInfo().getPort());
        }

        post.body(entries.toString());

        return execute(post);
    }
}
