package cn.chahuyun.docking

import cn.chahuyun.docking.MessageCache.getNowTime
import cn.chahuyun.docking.config.PluginConfig
import cn.hutool.core.date.DateUtil
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.UserOrBot
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.source


interface Cache {

    /**
     * 缓存这条消息
     */
    fun cache(event: GroupMessageEvent)

    /**
     * 缓存bot的消息
     */
    fun cache(messageCache: MessageCacheEntity)


    /**
     * 获取这个对象的消息缓存
     */
    fun getCachedMessages(group: Long): List<MessageCacheEntity>

}

object MessageCache : Cache {
    /**
     * 缓存实现
     */
    private lateinit var cache: Cache


    /**
     * 初始化
     */
    fun init() {
        val config = PluginConfig
        when (config.messageCache) {
            CacheType.MEMORY -> cache = MemoryCache(config.messageCacheNum)
            CacheType.REDIS -> TODO()
        }
    }


    /**
     * 缓存这条消息
     */
    override fun cache(event: GroupMessageEvent) {
        cache.cache(event)
    }

    /**
     * 缓存bot的消息
     */
    override fun cache(messageCache: MessageCacheEntity) {
        cache.cache(messageCache)
    }


    /**
     * 获取这个对象的消息缓存
     */
    override fun getCachedMessages(group: Long): List<MessageCacheEntity> {
        return cache.getCachedMessages(group)
    }

    fun getNowTime(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }


}


class MemoryCache(
    /**
     * 默认缓存数量
     */
    private val cacheNumber: Int,
) : Cache {


    /**
     * 内存缓存，使用 LinkedHashMap 来维护 Subject 和其对应的消息链列表
     */
    private val memoryCache: MutableMap<Long, MutableList<MessageCacheEntity>> = LinkedHashMap()

    /**
     * 缓存这条消息
     */
    override fun cache(event: GroupMessageEvent) {
        // 假设 message 中包含 subject 和 messageChain
        val group = event.subject
        val member = event.sender
        val messageChain = event.message
        val msgId = messageChain.source.ids[0]
        val time = getNowTime()

        // 获取或初始化该 subject 对应的消息列表
        val cachedMessages = memoryCache.getOrPut(group.id) { mutableListOf() }

        // 将新的消息添加到列表末尾
        cachedMessages.add(MessageCacheEntity(group, member, msgId, time, messageChain.contentToString(), MessageActionType.MSG))

        // 如果缓存数量超过设定的最大值，则移除最早的消息
        if (cachedMessages.size > cacheNumber) {
            cachedMessages.removeAt(0) // 移除最早的消息（FIFO 策略）
        }
    }

    /**
     * 缓存bot的消息
     */
    override fun cache(messageCache: MessageCacheEntity) {
        // 获取或初始化该 subject 对应的消息列表
        val cachedMessages = memoryCache.getOrPut(messageCache.group) { mutableListOf() }

        // 将新的消息添加到列表末尾
        cachedMessages.add(messageCache)

        // 如果缓存数量超过设定的最大值，则移除最早的消息
        if (cachedMessages.size > cacheNumber) {
            cachedMessages.removeAt(0) // 移除最早的消息（FIFO 策略）
        }
    }


    /**
     * 获取某个主题的缓存消息
     */
    override fun getCachedMessages(group: Long): List<MessageCacheEntity> {
        return memoryCache[group] ?: emptyList()
    }


}

/**
 * 消息缓存信息
 */
data class MessageCacheEntity(
    /**
     * 群
     */
    val group: Long,
    /**
     * 成员
     */
    val member: UserOrBot,
    /**
     * 消息id
     */
    val msgId: Int,
    /**
     * 时间
     */
    val time: String,
    /**
     * 消息内容
     */
    val message: String,
    /**
     * 操作
     */
    val action: MessageActionType,
) {
    /**
     * 构造器
     * @param group 群
     * @param member 成员
     */
    constructor(group: Group, member: UserOrBot, msgId: Int, time: Int, message: String, action: MessageActionType) :
            this(
                group.id,
                member,
                msgId,
                DateUtil.format(DateUtil.date(time * 1000L), "yyyy-MM-dd HH:mm:ss"),
                message,
                action
            )

}

enum class MessageActionType {
    /**
     * 发送消息
     */
    MSG,

    /**
     * 禁言
     */
    MUTE
}




