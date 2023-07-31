package cn.chahuyun.dockingGpt.docking;

import cn.chahuyun.authorize.utils.HibernateUtil;
import cn.chahuyun.dockingGpt.entity.MessageInfo;

/**
 * 请求接口
 *
 * @author Moyuyanli
 * @Date 2023/7/30 0:58
 */
public interface OpenAiRequest {

    /**
     * 通过 消息信息{@link MessageInfo} 进行判断，自动实现提问或对话消息请求
     *
     * @param info 消息信息
     * @return java.lang.String 返回结果
     * @author Moyuyanli
     * @date 2023/7/30 1:11
     */
    public String msgRequest(MessageInfo info);

}