package cn.chahuyun.docking

import cn.chahuyun.docking.Docking.log
import cn.chahuyun.docking.config.AiConfig
import cn.chahuyun.docking.config.OpenAiConfig
import cn.chahuyun.docking.config.PluginConfig
import cn.chahuyun.docking.entity.*
import cn.chahuyun.docking.http.RetrofitApi
import cn.chahuyun.hibernateplus.HibernateFactory
import cn.hutool.core.date.DateUtil
import cn.hutool.json.JSONObject
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.MemberPermission.*
import net.mamoe.mirai.contact.UserOrBot
import net.mamoe.mirai.contact.nameCardOrNick
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Client(
    /**
     * 连接秘钥
     */
    var apiKey: String,
    /**
     * 设定
     */
    var person: String,
    /**
     * 混乱
     */
    var temperature: Double,
    /**
     * 模型
     */
    var model: String,
    /**
     * 渠道
     */
    var baseUrl: String,
    /**
     * 代理
     */
    var proxyInfo: ProxyInfo,

    ) {

    private lateinit var httpServer: RetrofitApi

    private fun getToken(): String {
        return "Bearer $apiKey"
    }

    /**
     * 构建消息
     */
    fun build(question: QuestionMessage, position: String): String {
        val set = "{职位}"
        log.debug("bot 请求设定 $set")
        val requestInfo = RequestInfo(
            model,
            temperature,
            mutableListOf(
                Record(RoleType.SYSTEM, aiMessageSet.replace(set, position)),
                Record(RoleType.SYSTEM, person)
            )
        )

        val cached = MessageCache.getCachedMessages(group = question.group!!)

        val records = cached.map { handleMemberMessage(it.first, it.second) }

        log.debug("消息信息:${records[0]}")

        records.forEach { requestInfo.addMessage(it) }

        /*
        //过去式关联消息

        //, *spliceMessage.toTypedArray()

        var spliceMessage = spliceMessage(question)
        val size = spliceMessage.size
        if (size >= 10) {
            spliceMessage = spliceMessage.subList(size - 10, size)
        }
         */


        val response = question(getToken(), requestInfo)
        val execute = response.execute()
        if (execute.code() == 200) {
            val body = execute.body()

            return body?.message ?: "请求错误，无返回结果!"
        } else {
            val error = execute.errorBody()

            return error?.let { JSONObject(it.string()).getStr("code") } ?: "error: ${execute.code()}"
        }
    }

    /**
     * 根据消息链递归构造回复消息
     */
    private fun spliceMessage(question: QuestionMessage): MutableList<Record> {
        // 如果当前消息有引用ID，则获取该引用消息并递归处理
        if (question.quoteId != null) {
            val one = HibernateFactory.selectOne(QuestionMessage::class.java, "replyId", question.quoteId)
            // 递归直到找到没有引用ID的消息为止
            if (one?.quoteId != null) {
                // 递归处理引用消息，并将其结果与当前消息的结果合并
                return spliceMessage(one).apply {
                    question.question?.let { add(Record(RoleType.USER, it)) }
                    question.reply?.let { add(Record(RoleType.ROLE, it)) }
                }
            }
        }
        // 创建一个新的记录列表，并添加当前消息的信息
        val list = mutableListOf<Record>().apply {
            question.question?.let { add(Record(RoleType.USER, it)) }
            question.reply?.let { add(Record(RoleType.ROLE, it)) }
        }
        return list
    }


    /**
     * 向ai接口发送请求
     *
     * @param token       token
     * @param requestInfo 请求信息
     * @return retrofit2.Call<okhttp3.ResponseBody>
     * @author Moyuyanli
     * @date 2023/10/19 14:10
     */
    private fun question(token: String, requestInfo: RequestInfo): Call<ResponseInfo?> {
        if (!::httpServer.isInitialized) {
            val timeout = 30
            val client = OkHttpClient.Builder()
                .connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
                .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(timeout.toLong(), TimeUnit.SECONDS)
                .build()
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            httpServer = retrofit.create(RetrofitApi::class.java)
        }

        return httpServer.question(token, requestInfo)
    }

    /**
     * 处理构造下上文消息
     */
    private fun handleMemberMessage(member: UserOrBot, message: Triple<Int, Int, String>): Record {
        val (msgId, timeInt, msg) = message

        val time = DateUtil.format(DateUtil.date(timeInt / 1000L), "yyyy-MM-dd HH:mm:ss")
        if (member is Member) {
            val position = when (member.permission) {
                MEMBER -> "member"
                ADMINISTRATOR -> "admin"
                OWNER -> "owner"
            }

            // 格式化时间为字符串

            val prefix = "[id=${member.id},name=${member.nameCardOrNick},time=$time,msgId=$msgId,position=$position]\n"
            return Record(RoleType.USER, prefix + msg)
        }

        if (member is Bot) {
            val prefix = "[id=${member.id},name=${member.nameCardOrNick},time=$time,msgId=$msgId]\n"
            return Record(RoleType.ROLE, prefix + msg)
        }
        return Record(RoleType.ROLE, "这是一条空消息，请忽略!")
    }


}

object ClientFactory {

    private lateinit var requestClient: Client

    private lateinit var config: AiConfig

    fun init() {
        val type = PluginConfig.type

        if (type == SwitchType.XCJ) {
            val xcjConfig = OpenAiConfig.openAiConfig[SwitchType.XCJ]

            if (xcjConfig == null) {
                log.error("你没有该项配置!")
                return
            }

            config = xcjConfig

            val proxy = ProxyInfo(
                config.proxy[ProxyKey.Type] as String,
                config.proxy[ProxyKey.URL] as String,
                config.proxy[ProxyKey.Port] as Int
            )


            val client = Client(
                config.openAiKey,
                PersonManager.person + "\n以下为群聊内容，请你补全并进行回复。",
                config.temperature,
                config.openAiModel[config.defaultModel] ?: config.openAiModel.values.first(),
                config.openAiBaseUrl[config.defaultBaseUrl] ?: config.openAiBaseUrl.values.first(),
                proxy
            )

            requestClient = client

        } else {
            TODO()
        }
    }

    /**
     * 进行聊天
     */
    fun chat(question: QuestionMessage, position: String): String {
        return requestClient.build(question, position)
    }


    /**
     * 切换模型
     */
    fun switchModel(model: String): String {
        val s = config.openAiModel[model]
        return s?.let {
            requestClient.model = it
            config.defaultModel = model
            "模型切换成功:$model"
        } ?: "没有这个模型!"
    }

    /**
     * 当前模型
     */
    fun viewModel(): String {
        val model = requestClient.model

        config.openAiModel.forEach {
            if (it.value == model) {
                return "当前模型 ${it.key}"
            }
        }
        return "当前模型为空!"
    }

    /**
     * 查看模型列表
     */
    fun listModel(): String {
        var result = "当前拥有模型列表:\n"
        config.openAiModel.forEach {
            result += "${it.key}\n"
        }
        return result
    }

    /**
     * 添加模型
     */
    fun addModel(name: String, model: String): String {
        return config.openAiModel[name]?.let {
            "模型 $name 已经存在!"
        } ?: let {
            config.openAiModel[name] = model
            "模型 $name 添加成功！"
        }
    }


}