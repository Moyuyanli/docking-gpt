package cn.chahuyun.dockingGpt

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import net.mamoe.yamlkt.Comment

@ValueDescription("openAi配置")
object OpenAiConfig : AutoSavePluginConfig("openAiConfig"){

    @Serializable
    data class OpenAiConfig(
        @Comment("openAi提交信息")
        val submitInfo: SubmitInfo,
        @Comment("openAi秘钥")
        val openAiKey: String,
        @Comment("openAi模型")
        val openAiModel: String,
        @Comment("openAi混乱值(0~2)")
        val temperature: Double,
        @Comment("ai系统设定文件名称")
        val aiSystemConfig: String,
    )

    @Serializable
    data class SubmitInfo(
        @Comment("提交地址(http://127.0.0.1:33669)")
        val submitUrl: String,
        @Comment("代理方式(NONE,HTTP)")
        val proxyType: ProxyType,
        @Comment("代理地址(127.0.0.1)")
        val url: String,
        @Comment("代理端口(7890)")
        val port: String,
    ) {
        constructor(submitUrl: String, proxyType: ProxyType) : this(submitUrl, proxyType, "127.0.0.1", "7890")
        @Serializable
        enum class ProxyType {
            NONE,
            HTTP
        }
    }

    enum class SwitchType{
        HD,
        OFFICIAL,
        OTHER
    }

    @ValueDescription("openAi配置项")
    val openAiConfig: MutableMap<SwitchType,OpenAiConfig> by value(
        mutableMapOf(
            Pair(SwitchType.HD,OpenAiConfig(
                submitInfo = SubmitInfo(
                    submitUrl = "https://htgpt.646325.xyz",
                    proxyType = SubmitInfo.ProxyType.NONE
                ),
                openAiKey = "你的openAI秘钥",
                openAiModel = "gpt-3.5-turbo-16k-0613",
                temperature = 0.0,
                aiSystemConfig = "SystemConfig"
            )),
            Pair(SwitchType.OFFICIAL,OpenAiConfig(
                submitInfo = SubmitInfo(
                    submitUrl = "https://api.openai.com",
                    proxyType = SubmitInfo.ProxyType.HTTP,
                    url = "127.0.0.1",
                    port = "7890",
                ),
                openAiKey = "你的openAI秘钥",
                openAiModel = "gpt-3.5-turbo-16k-0613",
                temperature = 0.0,
                aiSystemConfig = "SystemConfig"
            )),
            Pair(SwitchType.OTHER,OpenAiConfig(
                submitInfo = SubmitInfo(
                    submitUrl = "其他地址",
                    proxyType = SubmitInfo.ProxyType.HTTP,
                    url = "127.0.0.1",
                    port = "7890",
                ),
                openAiKey = "你的openAI秘钥",
                openAiModel = "gpt-3.5-turbo-16k-0613",
                temperature = 0.0,
                aiSystemConfig = "SystemConfig"
            ))
        )
    )


}