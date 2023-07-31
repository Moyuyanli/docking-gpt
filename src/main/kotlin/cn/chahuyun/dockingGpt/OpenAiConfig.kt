package cn.chahuyun.dockingGpt

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import net.mamoe.yamlkt.Comment
import net.mamoe.yamlkt.YamlDynamicSerializer

@ValueDescription("openAi配置")
object OpenAiConfig : AutoSavePluginConfig("openAiConfig"){

    @Serializable
    data class OpenAiConfig(
        @Comment("openAi秘钥")
        val openAiKey: String,
        @Comment("openAi模型")
        val openAiModel: String,
        @Comment("openAi混乱值(0~2)")
        val temperature: Double,
        @Comment("ai系统设定文件名称")
        val aiSystemConfig: String,
        @Comment("""
            代理设置：可用配置(区分大小写)
            Type : "NONE" / "HTTP"
            URL : "127.0.0.1"
            Port : "7890"
            """)
        val proxy : Map<ProxyKey,@Serializable(with = YamlDynamicSerializer::class) Any> = mapOf(
            ProxyKey.Type to "NONE",
            ProxyKey.URL to "127.0.0.1",
            ProxyKey.Port to "7890"
        )
    ){
        @Serializable
        enum class ProxyKey{
            Type,URL,Port
        }
    }

    enum class SwitchType{
        XCJ,
        OFFICIAL
    }

    @ValueDescription("openAi配置项 XCJ / OFFICIAL")
    val openAiConfig: MutableMap<SwitchType,OpenAiConfig> by value(
        mutableMapOf(
            Pair(SwitchType.OFFICIAL,OpenAiConfig(
                openAiKey = "你的openAI秘钥",
                openAiModel = "gpt-3.5-turbo-16k-0613",
                temperature = 0.0,
                aiSystemConfig = "SystemConfig",
                proxy = mapOf(
                    OpenAiConfig.ProxyKey.Type to "HTTP",
                    OpenAiConfig.ProxyKey.URL to "127.0.0.1",
                    OpenAiConfig.ProxyKey.Port to "7890"
                )
            ))
        )
    )


}