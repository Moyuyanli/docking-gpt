package cn.chahuyun.docking.plugin

import cn.chahuyun.docking.config.PluginConfig
import cn.chahuyun.hibernateplus.DriveType
import cn.chahuyun.hibernateplus.HibernatePlusService
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin

object DataLoader {

    /**
     * 加载数据库
     */
    fun loader(plugin: JvmPlugin) {
        val configuration = HibernatePlusService.createConfiguration(plugin::class.java)

        configuration.classLoader = plugin::class.java.classLoader
        configuration.packageName = "cn.chahuyun.docking.entity"

        val config = PluginConfig
        configuration.driveType = config.dataType
        when (config.dataType) {
            DriveType.MYSQL -> {
                configuration.address = config.mysqlUrl
                configuration.user = config.mysqlUser
                configuration.password = config.mysqlPassword
            }

            DriveType.H2 -> configuration.address = plugin.dataFolderPath.resolve("docking.h2.mv.db").toString()
            DriveType.SQLITE -> configuration.address = plugin.dataFolderPath.resolve("docking.mv.db").toString()
        }

        HibernatePlusService.loadingService(configuration)
        log.info("docking DateBase loaded success fully !")
    }

}