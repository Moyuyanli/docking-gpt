package cn.chahuyun.docking

import cn.chahuyun.authorize.PermissionServer
import cn.chahuyun.authorize.entity.Perm
import cn.chahuyun.authorize.utils.PermUtil
import cn.chahuyun.docking.Docking.log
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin

object PermManager {

    /**
     * 加载权限
     */
    fun init(plugin: JvmPlugin) {
        PermissionServer.registerPermCode(plugin, Perm(PermCode.CHAT, "ai聊天权限"))

        val util = PermUtil

        val one = util.selectPermGroupOneByName(PermCode.CHAT)

        if (one == null) {
            val group = util.talkPermGroupByName(PermCode.CHAT)
            val perm = util.takePerm(PermCode.CHAT)
            util.addPermToPermGroupByPermGroup(perm, group)
        }
        log.info("docking perm loading success !")
    }


}