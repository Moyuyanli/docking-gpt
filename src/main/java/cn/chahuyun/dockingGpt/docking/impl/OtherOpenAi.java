package cn.chahuyun.dockingGpt.docking.impl;

import cn.chahuyun.dockingGpt.docking.AbstractRequest;
import cn.chahuyun.dockingGpt.entity.MessageInfo;
import cn.chahuyun.dockingGpt.entity.ProxyInfo;

/**
 * 使用其他openAi地址
 *
 * @author Moyuyanli
 * @Date 2023/7/30 1:19
 */
public class OtherOpenAi  extends AbstractRequest {


    public OtherOpenAi(String aiKey, String setAi, double temperature, String model, ProxyInfo proxyInfo) {
        super(aiKey, setAi, temperature, model, proxyInfo);
    }

    @Override
    public String msgRequest(MessageInfo info) {
        return null;
    }
}
