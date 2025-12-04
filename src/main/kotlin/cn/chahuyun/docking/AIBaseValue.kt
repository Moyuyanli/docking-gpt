package cn.chahuyun.docking

import cn.chahuyun.docking.config.AIConfig
import cn.chahuyun.docking.config.PluginConfig
import io.github.oshai.kotlinlogging.KotlinLogging

object BaseValue {
    private val log = KotlinLogging.logger { }

    val model = AIConfig.aiConfig[PluginConfig.type] ?: error("默认启用ai配置不存在!请检查配置!")

    /**
     * 获取默认AI配置的默认渠道下的默认token
     *
     * 该属性用于获取当前插件类型对应的AI配置中，默认渠道的默认token值。
     * 如果无法获取到有效的token，则返回空字符串并记录警告日志。
     */
    var token: String = model.keyMap["def"]
        ?: "".also { log.warn { "默认渠道下没有默认token" } }

    /**
     * 从model中获取temperature值，如果为空则使用默认值0.6
     */
    var temperature = model.temperature


    /**
     * 获取默认模型名称
     *
     * @return 返回默认模型名称，如果默认模型不存在则返回第一个非空模型名称
     */
    fun getAIModel(): String = model.modelMap[model.defaultModel] ?: model.modelMap.firstNotNullOf { it.value }.also {
        log.warn { "默认模型对应的模型名称不存,已自动顺位使用 $it " }
    }


}