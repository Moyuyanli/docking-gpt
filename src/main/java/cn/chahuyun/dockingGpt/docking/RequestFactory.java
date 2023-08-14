package cn.chahuyun.dockingGpt.docking;

import cn.chahuyun.authorize.utils.Log;
import cn.chahuyun.dockingGpt.DockingGPT;
import cn.chahuyun.dockingGpt.OpenAiConfig;
import cn.chahuyun.dockingGpt.PluginConfig;
import cn.chahuyun.dockingGpt.component.utils.PersonUtil;
import cn.chahuyun.dockingGpt.constant.Constant;
import cn.chahuyun.dockingGpt.docking.impl.XCJOpenAi;
import cn.chahuyun.dockingGpt.entity.ProxyInfo;
import kotlinx.serialization.Serializable;

import java.util.Map;

/**
 * 消息请求工厂
 *
 * @author Moyuyanli
 * @Date 2023/7/30 2:00
 */
public class RequestFactory {

    /**
     * ai人设
     */
    private String setGpt;

    /**
     * 请求接口
     */
    private OpenAiRequest openAiRequest;

    /**
     * 插件设置
     */
    private PluginConfig pluginConfig;

    /**
     * openai设置
     */
    private Map<OpenAiConfig.SwitchType, OpenAiConfig.OpenAiConfig> openAiConfig;

    private static final RequestFactory INSTANCE = new RequestFactory();


    /**
     * 获取请求工厂实例
     *
     * @return cn.chahuyun.dockingGpt.docking.RequestFactory
     * @author Moyuyanli
     * @date 2023/8/12 22:42
     */
    public static RequestFactory create() {
        return INSTANCE;
    }

    /**
     * 加载工厂
     *
     * @author Moyuyanli
     * @date 2023/8/12 22:42
     */
    public void init() {
        pluginConfig = DockingGPT.pluginConfig;
        openAiConfig = OpenAiConfig.INSTANCE.getOpenAiConfig();


        OpenAiConfig.OpenAiConfig aiConfig;
        OpenAiConfig.SwitchType aiType = pluginConfig.getType();

        ProxyInfo proxyInfo = null;

        switch (aiType) {
            case XCJ:
                aiConfig = openAiConfig.get(OpenAiConfig.SwitchType.XCJ);
                Map<OpenAiConfig.OpenAiConfig.ProxyKey, @Serializable Object> proxy = aiConfig.getProxy();

                String string = proxy.get(OpenAiConfig.OpenAiConfig.ProxyKey.Type).toString();
                if (string.equals(Constant.HTTP)) {
                    String url = proxy.get(OpenAiConfig.OpenAiConfig.ProxyKey.URL).toString();
                    String port = proxy.get(OpenAiConfig.OpenAiConfig.ProxyKey.Port).toString();
                    proxyInfo = new ProxyInfo(Constant.HTTP, url, port);
                }

                String person = PersonUtil.getPerson(aiConfig.getAiSystemConfig());
                openAiRequest = new XCJOpenAi(aiConfig.getOpenAiKey(), person,aiConfig.getTemperature(),aiConfig.getOpenAiModel(),proxyInfo);
                break;
            case OFFICIAL:
            default:
                Log.error("openAi类型错误！");
        }
    }

    /**
     * 根据配置获取gpt请求工具
     *
     * @return cn.chahuyun.dockingGpt.docking.AbstractRequest
     * @author Moyuyanli
     * @date 2023/8/12 23:37
     */
    public AbstractRequest getOpenAiRequest() {
        return (AbstractRequest) openAiRequest;
    }
}
