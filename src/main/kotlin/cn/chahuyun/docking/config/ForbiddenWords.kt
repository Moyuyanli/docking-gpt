package cn.chahuyun.docking.config

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

    @ValueDescription("过滤词")
    var forbidden: MutableList<String> by value(mutableListOf("不[能要行]"))


}
