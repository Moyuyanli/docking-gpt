package cn.chahuyun.docking

import cn.chahuyun.docking.config.PluginConfig
import cn.hutool.core.date.DateUtil
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.UserOrBot
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.source


interface Cache {

    /**
     * 缓存这条消息
     */
    fun cache(event: GroupMessageEvent)

    /**
     * 缓存bot的消息
     */
    fun cache(bot: Bot, group: Group, chat: MessageReceipt<Contact>)

    /**
     * 缓存bot的消息
     */
    fun cache(bot: Bot, group: Group, chat: String)


    /**
     * 获取这个对象的消息缓存
     */
    fun getCachedMessages(group: Long): List<Pair<UserOrBot, Triple<Int, Int, String>>>

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
    override fun cache(bot: Bot, group: Group, chat: MessageReceipt<Contact>) {
        cache.cache(bot, group, chat)
    }

    /**
     * 缓存bot的消息
     */
    override fun cache(bot: Bot, group: Group, chat: String) {
        cache.cache(bot, group, chat)
    }

    /**
     * 获取这个对象的消息缓存
     */
    override fun getCachedMessages(group: Long): List<Pair<UserOrBot, Triple<Int, Int, String>>> {
        return cache.getCachedMessages(group)
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
    private val memoryCache: MutableMap<Long, MutableList<Pair<UserOrBot, Triple<Int, Int, String>>>> = LinkedHashMap()

    /**
     * 缓存这条消息
     */
    override fun cache(event: GroupMessageEvent) {
        // 假设 message 中包含 subject 和 messageChain
        val group = event.subject
        val member = event.sender
        val messageChain = event.message
        val msgId = messageChain.source.ids[0]
        val time = messageChain.source.time

        // 获取或初始化该 subject 对应的消息列表
        val cachedMessages = memoryCache.getOrPut(group.id) { mutableListOf() }

        // 将新的消息添加到列表末尾
        cachedMessages.add(Pair(member, Triple(msgId, time, messageChain.contentToString())))

        // 如果缓存数量超过设定的最大值，则移除最早的消息
        if (cachedMessages.size > cacheNumber) {
            cachedMessages.removeAt(0) // 移除最早的消息（FIFO 策略）
        }
    }

    /**
     * 缓存bot的消息
     */
    override fun cache(bot: Bot, group: Group, chat: MessageReceipt<Contact>) {
        // 获取或初始化该 subject 对应的消息列表
        val cachedMessages = memoryCache.getOrPut(group.id) { mutableListOf() }

        val msgId = chat.source.ids[0]
        val time = chat.source.time
        val string = chat.source.contentToString()

        // 将新的消息添加到列表末尾
        cachedMessages.add(Pair(bot, Triple(msgId, time, string)))

        // 如果缓存数量超过设定的最大值，则移除最早的消息
        if (cachedMessages.size > cacheNumber) {
            cachedMessages.removeAt(0) // 移除最早的消息（FIFO 策略）
        }
    }

    /**
     * 缓存bot的消息
     */
    override fun cache(bot: Bot, group: Group, chat: String) {
        val cachedMessages = memoryCache.getOrPut(group.id) { mutableListOf() }

        // 将新的消息添加到列表末尾
        cachedMessages.add(Pair(bot, Triple(-1, DateUtil.date().time.toInt(), chat)))

        // 如果缓存数量超过设定的最大值，则移除最早的消息
        if (cachedMessages.size > cacheNumber) {
            cachedMessages.removeAt(0) // 移除最早的消息（FIFO 策略）
        }

    }

    /**
     * 获取某个主题的缓存消息
     */
    override fun getCachedMessages(group: Long): List<Pair<UserOrBot, Triple<Int, Int, String>>> {
        return memoryCache[group] ?: emptyList()
    }

}




