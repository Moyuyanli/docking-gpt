package cn.chahuyun.dockingGpt

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

/**
 * 禁词
 *
 * @author Moyuyanli
 * @date 2024/4/12 9:33
 */
object ForbiddenWords : AutoSavePluginConfig("forbidden") {

    @ValueDescription("openAi配置项 XCJ / OFFICIAL")
    var forbidden: List<String> by value(arrayListOf<String>().apply {
        add("不[能要行]")
    })


}
