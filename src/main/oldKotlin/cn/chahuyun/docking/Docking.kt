package cn.chahuyun.docking

import cn.chahuyun.authorize.PermissionServer
import cn.chahuyun.docking.config.ForbiddenWords
import cn.chahuyun.docking.config.OpenAiConfig
import cn.chahuyun.docking.config.PluginConfig
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

object Docking : KotlinPlugin(
    JvmPluginDescription(
        id = "cn.chahuyun.docking",
        version = BuildConstants.VERSION,
        name = "Docking"
    ) {
        author("moyuyanli")
        info("用于对接AI的Mira插件")
            dependsOn("cn.chahuyun.HuYanAuthorize", ">=1.2.0", false)
    }
) {
    /**
     * 日志
     */
    val log = this.logger

    /**
     * 在插件被启用时调用, 可能会被调用多次
     */
    override fun onEnable() {
        PluginConfig.reload()
        OpenAiConfig.reload()
        ForbiddenWords.reload()

        PermManager.init(this)
        PluginManager.init(this)
        PersonManager.init(this)
        MessageCache.init()

        //初始化聊天工厂
        ClientFactory.init()

        PermissionServer.registerMessageEvent(this, "cn.chahuyun.docking.event")
    }

    /**
     * 在插件被关闭时调用, 可能会被调用多次
     */
    override fun onDisable() {

    }
}