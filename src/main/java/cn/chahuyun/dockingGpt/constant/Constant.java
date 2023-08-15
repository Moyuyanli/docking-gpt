package cn.chahuyun.dockingGpt.constant;

/**
 * 常用常量
 *
 * @author Moyuyanli
 * @Date 2023/8/12 23:17
 */
public class Constant {

    /**
     * 目前拥有的代理类型
     */
    public static final String NONE = "none";
    public static final String HTTP = "http";




    /**
     * ai设定-模型
     */
    public static final String AI_SET_MODEL = "model";
    /**
     * ai设定-混乱值
     */
    public static final String AI_SET_TEMPERATURE = "temperature";





    /**
     * xcj-Ai地址
     */
    public static final String XCJ_AI_URL = "https://htgpt.646325.xyz/v1/chat/completions";

    /**
     * 官方-AI地址
     */
    public static final String OFFICIAL_AI_URL = "https://api.openai.com/v1/chat/completions";

    /**
     * http请求-参数类型
     */
    public static final String REQUEST_HEAD_TYPE = "application/json";
    /**
     * http请求-结果-成功
     */
    public static final int REQUEST_RESULT_STATUS_SUCCESS = 200;


}
