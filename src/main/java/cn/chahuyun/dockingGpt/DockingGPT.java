package cn.chahuyun.dockingGpt;


import cn.chahuyun.authorize.PermissionServer;
import cn.chahuyun.authorize.utils.Log;
import cn.chahuyun.dockingGpt.component.utils.PersonUtil;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

/**
 * @author Moyuyanli
 */
public final class DockingGPT extends JavaPlugin {

    /**
     * 插件唯一实例
     */
    public static final DockingGPT INSTANCE = new DockingGPT();

    /**
     * 插件配置
     */
    public static PluginConfig pluginConfig;


    private DockingGPT() {
        super(new JvmPluginDescriptionBuilder("cn.chahuyun.docking-gpt", "0.1.0")
                .name("Docking-GPT")
                .info("用于对接openAI的Mira插件")
                .dependsOn("cn.chahuyun.HuYanAuthorize",false)
                .dependsOn("xyz.cssxsh.mirai.plugin.mirai-hibernate-plugin", false)
                .author("Moyuyanli")
                .build());
    }

    @Override
    public void onEnable() {

        reloadPluginConfig(OpenAiConfig.INSTANCE);
        reloadPluginConfig(PluginConfig.INSTANCE);

        pluginConfig = PluginConfig.INSTANCE;
        PersonUtil.init();
        Log.init(INSTANCE);
        PermissionServer instance = PermissionServer.getInstance();
        instance.addPermission("chat", "chatGPT聊天权限");
        instance.init(INSTANCE, "cn.chahuyun.dockingGpt.event");
        getLogger().info("Docking-GPT Start-up success!");
    }
}