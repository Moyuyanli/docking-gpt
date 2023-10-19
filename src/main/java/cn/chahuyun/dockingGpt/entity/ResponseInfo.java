package cn.chahuyun.dockingGpt.entity;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求结果实体
 *
 * @author Moyuyanli
 * @date 2023/10/19 14:37
 */
@Data
public class ResponseInfo {

    private String id;
    private String object;
    private Integer created;
    private String model;
    private Usage usage;
    private List<ResultMessage> choices = new ArrayList<>();

    /**
     * 获取回答(第一条消息)
     * @return 回答
     */
    public String getMessage() {
        return getMessage(0);
    }

    /**
     * 获取对应下标的结果信息
     * @param index 下标
     * @return 回答
     */
    public String getMessage(int index) {
        if (choices.isEmpty()) {
            return "错误:返回结果为空！";
        }
        return choices.get(index).getMessage().getContent();
    }

    /**
     * 转换为json对象
     *
     * @return cn.hutool.json.JSONObject
     * @author Moyuyanli
     * @date 2023/10/19 14:15
     */
    public JSONObject toJson() {
        return JSONUtil.parseObj(this);
    }
}

