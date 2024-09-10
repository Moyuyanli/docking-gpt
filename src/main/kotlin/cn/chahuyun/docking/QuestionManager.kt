package cn.chahuyun.docking

import cn.chahuyun.docking.Docking.log
import cn.chahuyun.docking.config.AiConfig
import cn.chahuyun.docking.config.OpenAiConfig
import cn.chahuyun.docking.config.PluginConfig
import cn.chahuyun.docking.entity.*
import cn.chahuyun.docking.http.RetrofitApi
import cn.chahuyun.hibernateplus.HibernateFactory
import cn.hutool.json.JSONObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RequestImpl(
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

    private fun getToken(): String {
        return "Bearer $apiKey"
    }

    /**
     * 构建消息
     */
    fun build(question: QuestionMessage): String {
        var spliceMessage = spliceMessage(question)
        val size = spliceMessage.size
        if (size >= 10) {
            spliceMessage = spliceMessage.subList(size - 10, size)
        }

        val requestInfo = RequestInfo(
            model,
            temperature,
            mutableListOf(Record(RoleEnum.SYSTEM, person), *spliceMessage.toTypedArray())
        )

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
                    question.question?.let { add(Record(RoleEnum.USER, it)) }
                    question.reply?.let { add(Record(RoleEnum.ROLE, it)) }
                }
            }
        }
        // 创建一个新的记录列表，并添加当前消息的信息
        val list = mutableListOf<Record>().apply {
            question.question?.let { add(Record(RoleEnum.USER, it)) }
            question.reply?.let { add(Record(RoleEnum.ROLE, it)) }
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

        return retrofit.create(RetrofitApi::class.java).question(token, requestInfo)
    }
}

object ChatFactory {

    private lateinit var requestExamples: RequestImpl

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


            val impl = RequestImpl(
                config.openAiKey,
                PersonManager.person,
                config.temperature,
                config.openAiModel.values.first(),
                config.openAiBaseUrl.values.first(),
                proxy
            )

            requestExamples = impl

        } else {
            TODO()
        }
    }

    /**
     * 进行聊天
     */
    fun chat(question: QuestionMessage): String {
        return requestExamples.build(question)
    }


    /**
     * 切换模型
     */
    fun switchModel(model: String): String {
        val s = config.openAiModel[model]
        return s?.let {
            requestExamples.model = it
            "模型切换成功:$model"
        } ?: "没有这个模型!"
    }

    /**
     * 当前模型
     */
    fun viewModel(): String {
        val model = requestExamples.model

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
            result += it.key
        }
        return result
    }

}