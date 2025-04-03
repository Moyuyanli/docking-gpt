package cn.chahuyun.docking.config

import cn.chahuyun.docking.ProxyKey
import cn.chahuyun.docking.SwitchType
import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import net.mamoe.yamlkt.Comment
import net.mamoe.yamlkt.YamlDynamicSerializer

@ValueDescription("openAi配置")
object OpenAiConfig : AutoSavePluginConfig("openAiConfig") {

    @ValueDescription("openAi配置项 XCJ / CUSTOM / OFFICIAL")
    val openAiConfig: MutableMap<SwitchType, AiConfig> by value(
        mutableMapOf(
            Pair(
                SwitchType.OFFICIAL, AiConfig(
                    defaultBaseUrl= "默认",
                    openAiBaseUrl = mutableMapOf("默认" to "你的的baseUrl"),
                    openAiKey = "你的openAI秘钥",
                    defaultModel = "默认",
                    openAiModel = mutableMapOf("gpt-3.5" to "gpt-3.5-turbo-16k-0613"),
                    temperature = 0.0,
                    proxy = mutableMapOf(
                        ProxyKey.Type to "HTTP",
                        ProxyKey.URL to "127.0.0.1",
                        ProxyKey.Port to 7890
                    )
                )
            )
        )
    )

}

@Serializable
data class AiConfig(
    @Comment("默认渠道")
    val defaultBaseUrl: String,
    @Comment("openAi渠道")
    val openAiBaseUrl: MutableMap<String, String>,
    @Comment("openAi秘钥")
    val openAiKey: String,
    @Comment("默认模型")
    var defaultModel: String,
    @Comment("openAi模型")
    val openAiModel: MutableMap<String, String>,
    @Comment("openAi混乱值(0~2)")
    val temperature: Double,
    @Comment(
        """
            代理设置：可用配置(区分大小写)
            Type : "NONE" / "HTTP"
            URL : "127.0.0.1"
            Port : 7890
            """
    )
    val proxy: MutableMap<ProxyKey, @Serializable(with = YamlDynamicSerializer::class) Any> = mutableMapOf(
        ProxyKey.Type to "NONE",
        ProxyKey.URL to "127.0.0.1",
        ProxyKey.Port to 7890
    ),
)

