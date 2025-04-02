package cn.chahuyun.docking.config

import cn.chahuyun.docking.CacheType
import cn.chahuyun.docking.ReplyType
import cn.chahuyun.docking.SwitchType
import cn.chahuyun.hibernateplus.DriveType
import lombok.Data
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import net.mamoe.yamlkt.Comment


@Data
@ValueDescription("插件配置")
object PluginConfig : AutoSavePluginConfig("config") {

    @ValueDescription("使用的openAi配置项(XCJ,OFFICIAL)")
    val type: SwitchType by value(SwitchType.XCJ)

    @Comment(
        """
        默认的消息触发词,通过正则处理.
        匹配正则:'^{trigger}.*'
        """
    )
    val trigger: String by value("小狐狸")

    @ValueDescription("群未激活时，随机回复概率:(0~100)")
    val notActivatedProbability:Int by value(5)

    @ValueDescription("激活后回复模式(RANDOM:随机,TIMES:固定次数)")
    val activatedReplyMode:Pair<ReplyType,Int> by value(Pair(ReplyType.RANDOM,40))

    @ValueDescription("数据库类型(H2,SQLITE,MYSQL)")
    var dataType: DriveType by value(DriveType.H2)

    @ValueDescription("mysql数据库连接地址")
    val mysqlUrl: String by value("127.0.0.1:3306/docking")

    @ValueDescription("mysql数据库用户名")
    val mysqlUser: String by value("root")

    @ValueDescription("mysql数据库密码")
    val mysqlPassword: String by value("123456")

    @ValueDescription("消息缓存位置(MEMORY:内存;REDIS:redis)")
    val messageCache:CacheType by value(CacheType.MEMORY)

    @ValueDescription("消息缓存数量(默认20条)")
    val messageCacheNum:Int by  value(20)




}