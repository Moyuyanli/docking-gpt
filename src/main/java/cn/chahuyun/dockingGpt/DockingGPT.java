package cn.chahuyun.dockingGpt;


import cn.chahuyun.authorize.PermissionServer;
import cn.chahuyun.dockingGpt.component.utils.Log;
import cn.chahuyun.dockingGpt.component.utils.PersonUtil;
import cn.hutool.core.util.RandomUtil;
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

    public static final String VERSION = "1.0.4";

    private DockingGPT() {
        super(new JvmPluginDescriptionBuilder("cn.chahuyun.docking-gpt", VERSION)
                .name("Docking-GPT")
                .info("用于对接openAI的Mira插件")
                .dependsOn("cn.chahuyun.HuYanAuthorize", false)
                .dependsOn("xyz.cssxsh.mirai.plugin.mirai-hibernate-plugin", false)
                .author("Moyuyanli")
                .build());
    }

    @Override
    public void onEnable() {
        /*
        加载配置
         */
        reloadPluginConfig(OpenAiConfig.INSTANCE);
        reloadPluginConfig(PluginConfig.INSTANCE);
        reloadPluginConfig(ForbiddenWords.INSTANCE);
        pluginConfig = PluginConfig.INSTANCE;

        //加载ai人设文件
        PersonUtil.init();
        //
        Log.init(INSTANCE);
        PermissionServer instance = PermissionServer.getInstance();
        instance.addPermission("chat", "chatGPT聊天权限");
        instance.addPermission("chat-admin", "chatGPT管理权限");
        instance.init(INSTANCE, "cn.chahuyun.dockingGpt.event");
        getLogger().info("Docking-GPT Start-up success!");
    }
}