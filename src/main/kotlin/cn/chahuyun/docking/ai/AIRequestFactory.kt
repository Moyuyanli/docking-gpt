package cn.chahuyun.docking.ai

import cn.chahuyun.docking.entity.AIRequest
import cn.chahuyun.docking.entity.Record

/**
 * AI请求工厂对象，用于创建AI请求实例
 */
object AIRequestFactory {

    private var request = AIRequest()

    /**
     * 创建AI请求对象
     *
     * @param list 消息记录列表，用于设置AI请求的消息内容
     * @return 返回一个新的AIRequest实例，其中包含指定的消息列表
     */
    fun createAiRequest(list: List<Record>): AIRequest = request.copy(messages = list)

    /**
     * 更新模型函数
     *
     * 该函数用于创建一个新的AI请求对象来更新当前的请求状态。
     * 通过重新初始化request变量，确保使用最新的配置和数据。
     */
    fun updateModel() {
        request = AIRequest()
    }

}