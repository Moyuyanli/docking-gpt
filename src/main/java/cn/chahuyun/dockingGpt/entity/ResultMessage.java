package cn.chahuyun.dockingGpt.entity;

import lombok.Data;

/**
 * 返回结果信息
 *
 * @author Moyuyanli
 * @date 2023/10/19 14:43
 */
@Data
public class ResultMessage {
    private Integer index;
    private String finish_reason;
    private OpenAiMessage message;
}
