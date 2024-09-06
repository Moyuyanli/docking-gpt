package cn.chahuyun.dockingGpt

import cn.chahuyun.authorize.utils.MessageUtil
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.MessageEvent

object DockingGPT : KotlinPlugin(
    JvmPluginDescription(
        id = "cn.chahuyun.docking-gpt",
        version = BuildConstants.VERSION,
        name = "DockingGPT"
    ){
        author("moyuyanli")
        info("用于对接openAI的Mira插件")
        dependsOn("cn.chahuyun.HuYanAuthorize",">=1.1.5",false)
    }
) {

    /**
     * 在插件被启用时调用, 可能会被调用多次
     */
    override fun onEnable() {
    }

    /**
     * 在插件被关闭时调用, 可能会被调用多次
     */
    override fun onDisable() {

    }
}