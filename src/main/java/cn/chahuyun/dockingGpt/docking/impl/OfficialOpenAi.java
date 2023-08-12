package cn.chahuyun.dockingGpt.docking.impl;

import cn.chahuyun.dockingGpt.docking.AbstractRequest;
import cn.chahuyun.dockingGpt.entity.MessageInfo;
import cn.chahuyun.dockingGpt.entity.ProxyInfo;

/**
 * 使用官方openAi地址
 *
 * @author Moyuyanli
 * @Date 2023/7/30 1:15
 */
public class OfficialOpenAi extends AbstractRequest  {


    public OfficialOpenAi(String aiKey, String setAi, String model, ProxyInfo proxyInfo) {
        super(aiKey, setAi, model, proxyInfo);
    }

    @Override
    public String msgRequest(MessageInfo info) {
        return null;
    }
}
