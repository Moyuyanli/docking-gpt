package cn.chahuyun.dockingGpt.docking;

import cn.chahuyun.dockingGpt.entity.ProxyInfo;
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
}
