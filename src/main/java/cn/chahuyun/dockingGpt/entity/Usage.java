package cn.chahuyun.dockingGpt.entity;

import lombok.Data;

/**
 * @author Moyuyanli
 * @date 2023/10/19 15:49
 */
@Data
public class Usage {
    private String prompt_tokens;
    private String completion_tokens;
    private String total_tokens;
}
