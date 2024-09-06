package cn.chahuyun.dockingGpt.event

import cn.chahuyun.authorize.EventComponent
import cn.chahuyun.authorize.MessageAuthorize
import cn.chahuyun.dockingGpt.CustomMatch
import cn.chahuyun.dockingGpt.PermCode
import net.mamoe.mirai.event.events.GroupMessageEvent

@EventComponent
class ChatEvent {


    @MessageAuthorize(
        custom = CustomMatch::class,
        groupPermissions = [PermCode.CHAT]
    )
    suspend fun chat(event: GroupMessageEvent) {

    }

}