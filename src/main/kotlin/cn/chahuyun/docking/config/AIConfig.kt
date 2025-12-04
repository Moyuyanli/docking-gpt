package cn.chahuyun.docking.config

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import net.mamoe.yamlkt.Comment
import net.mamoe.yamlkt.YamlDynamicSerializer

@ValueDescription("ai配置")
object AIConfig : AutoSavePluginConfig("aiConfig") {

    @ValueDescription("ai配置列表")
    val aiConfig: MutableMap<String, AIConfigModel> by value(
        mutableMapOf(
            "def" to AIConfigModel(
                defBaseUrl = "def",
                baseUrlMap = mutableMapOf("def" to "https://127.0.0.1"),
                keyMap = mutableMapOf("def" to "key"),
                defaultModel = "def",
                modelMap = mutableMapOf("def" to "Qwen-plus"),
                temperature = 0.6,
                proxy = mutableMapOf(
                    ProxyKey.Type to "NONE",
                    ProxyKey.URL to "127.0.0.1",
                    ProxyKey.Port to 7890
                )
            )
        )
    )

}

@Serializable
data class AIConfigModel(
    @Comment("默认渠道别名")
    var defBaseUrlAlias: String,
    @Comment("接入渠道 渠道别名 : 渠道地址")
    val baseUrlMap: MutableMap<String, String>,
    @Comment("接入秘钥 渠道别名 : 密钥")
    val keyMap: MutableMap<String, String>,
    @Comment("默认模型别名")
    var defaultModelAlias: String,
    @Comment("模型列表 模型别名 : 实际模型名称")
    val modelMap: MutableMap<String, String>,
    @Comment("混乱值(0~2),建议 0.6")
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

enum class ProxyKey {
    Type, URL, Port
}
