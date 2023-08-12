package cn.chahuyun.dockingGpt.docking;

import cn.chahuyun.dockingGpt.entity.ProxyInfo;

/**
 * openai抽象接口
 *
 * @author Moyuyanli
 * @Date 2023/8/12 22:47
 */
public abstract class AbstractRequest implements OpenAiRequest {


    private String aiKey;

    private String setAi;

    private String model;

    private ProxyInfo proxyInfo;

    public AbstractRequest(String aiKey, String setAi, String model, ProxyInfo proxyInfo) {
        this.aiKey = aiKey;
        this.setAi = setAi;
        this.model = model;
        this.proxyInfo = proxyInfo;
    }

    public String getSetAi() {
        return setAi;
    }

    public void setSetAi(String setAi) {
        this.setAi = setAi;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAiKey() {
        return aiKey;
    }

    public void setAiKey(String aiKey) {
        this.aiKey = aiKey;
    }

    public ProxyInfo getProxyInfo() {
        return proxyInfo;
    }

    public void setProxyInfo(ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
    }
}
