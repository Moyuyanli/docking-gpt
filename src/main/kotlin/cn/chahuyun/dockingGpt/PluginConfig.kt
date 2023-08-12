package cn.chahuyun.dockingGpt

import kotlinx.serialization.Serializable
import lombok.Data
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import net.mamoe.yamlkt.Comment


@Data
@ValueDescription("插件配置")
object PluginConfig : AutoSavePluginConfig("config") {

    @ValueDescription("使用的openAi配置项(XCJ,OFFICIAL)")
    val type : OpenAiConfig.SwitchType by value(OpenAiConfig.SwitchType.XCJ)

    @Comment("""
        默认的消息触发词,通过正则处理.
        匹配正则:'^{trigger}.*'
        """)
    val trigger : String by value("小狐狸")

    @Serializable
    enum class CacheType{
        MEMORY,
        REDIS,
        MYSQL
    }

    @ValueDescription("数据缓存位置(MEMORY-jvm内存，REDIS-redis，MYSQL-mysql")
    val cache: CacheType by value(CacheType.MEMORY)



}