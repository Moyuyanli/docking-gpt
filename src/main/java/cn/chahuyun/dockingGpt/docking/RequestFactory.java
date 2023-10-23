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

    private static RequestFactory instance;


    /**
     * 获取请求工厂实例
     *
     * @return cn.chahuyun.dockingGpt.docking.RequestFactory
     * @author Moyuyanli
     * @date 2023/8/12 22:42
     */
    public static RequestFactory create() {
        if (instance == null) {
            RequestFactory requestFactory = new RequestFactory();
            requestFactory.init();
            instance = requestFactory;
        }
        return instance;
    }


    /**
     * 修改ai模型
     *
     * @param modelKey 模型关键字
     * @return cn.chahuyun.dockingGpt.docking.RequestFactory
     * @author Moyuyanli
     * @date 2023/10/20 18:27
     */
    public RequestFactory setModel(String modelKey) {
        AbstractRequest request = (AbstractRequest) openAiRequest;
        String model;
        switch (modelKey) {
            case "gpt4.0":
                model = Constant.OPENAI_MODEL_GPT_4;
                break;
            case "通义千问":
                model = Constant.OPENAI_MODEL_TONG_YI_QIAN_WEN;
                break;
            case "文心一言":
                model = Constant.OPENAI_MODEL_WEN_XIN_YI_YAN;
                break;
            case "gpt3.5":
            default:
                model = Constant.OPENAI_MODEL_GPT_3_5;
        }
        request.setModel(model);
        return this;
    }

    /**
     * 查看当前模型
     *
     * @return 模型
     */
    public String getModel() {
        AbstractRequest request = (AbstractRequest) openAiRequest;
        switch (request.getModel()) {

            case Constant.OPENAI_MODEL_GPT_4:
                return "gpt4.0";
            case Constant.OPENAI_MODEL_TONG_YI_QIAN_WEN:
                return "通义千问";
            case Constant.OPENAI_MODEL_WEN_XIN_YI_YAN:
                return "文心一言";
            case Constant.OPENAI_MODEL_GPT_3_5:
            default:
                return "gpt3.5";
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


    //===================================private===================================


    /**
     * 加载工厂
     *
     * @author Moyuyanli
     * @date 2023/8/12 22:42
     */
    private void init() {
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
                    int port = (int) proxy.get(OpenAiConfig.OpenAiConfig.ProxyKey.Port);
                    proxyInfo = new ProxyInfo(Constant.HTTP, url, port);
                }

                String person = PersonUtil.getPerson(aiConfig.getAiSystemConfig());
                openAiRequest = new XCJOpenAi(aiConfig.getOpenAiKey(), person, aiConfig.getTemperature(), aiConfig.getOpenAiModel(), proxyInfo);
                break;
            case OFFICIAL:
            default:
                Log.error("openAi类型错误！");
        }
    }

}
