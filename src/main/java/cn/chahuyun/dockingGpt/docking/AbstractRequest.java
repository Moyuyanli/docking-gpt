package cn.chahuyun.dockingGpt.docking;

import cn.chahuyun.authorize.utils.Log;
import cn.chahuyun.dockingGpt.constant.Constant;
import cn.chahuyun.dockingGpt.constant.OpenAiMessageRoleEnum;
import cn.chahuyun.dockingGpt.entity.OpenAiMessage;
import cn.chahuyun.dockingGpt.entity.ProxyInfo;
import cn.chahuyun.dockingGpt.entity.RequestInfo;
import cn.chahuyun.dockingGpt.entity.ResponseInfo;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

/**
 * openai抽象接口
 *
 * @author Moyuyanli
 * @Date 2023/8/12 22:47
 */
@Data
public abstract class AbstractRequest implements OpenAiRequest {
    /**
     * 连接秘钥
     */
    private String aiKey;
    /**
     * ai设定
     */
    private String setAi;
    /**
     * 混乱值
     */
    private double temperature;
    /**
     * ai模型
     */
    private String model;
    /**
     * 代理信息
     */
    private ProxyInfo proxyInfo;

    public AbstractRequest(String aiKey, String setAi, double temperature, String model, ProxyInfo proxyInfo) {
        this.aiKey = aiKey;
        this.setAi = setAi;
        this.temperature = temperature;
        this.model = model;
        this.proxyInfo = proxyInfo;
    }

    /**
     * 获取Authorize
     *
     * @return token
     */
    public String getAuthorizeToken() {
        return "Bearer " + getAiKey();
    }

    /**
     * 获取消息构造
     *
     * @return entries
     */
    public RequestInfo getParams(String message) {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setModel(getModel());
        requestInfo.setTemperature(getTemperature());

        OpenAiMessage system = new OpenAiMessage(OpenAiMessageRoleEnum.SYSTEM, getSetAi());
        OpenAiMessage user = new OpenAiMessage(OpenAiMessageRoleEnum.USER, message);

        requestInfo.addMessage(system).addMessage(user);

        Log.debug("OpenAi request body ->" + requestInfo.toJson());
        return requestInfo;
    }

    /**
     * 同步执行http请求
     * <p>
     * 使用hutool的http
     *
     * @return 返回消息
     */
    @Deprecated
    public String execute(HttpRequest post) {
        try (HttpResponse httpResponse = post.execute()) {
            if (httpResponse.getStatus() == Constant.REQUEST_RESULT_STATUS_SUCCESS) {
                JSONObject response = JSONUtil.parseObj(httpResponse.body());
                JSONArray choices = response.getJSONArray("choices");
                JSONObject jsonObject = choices.getJSONObject(0);
                JSONObject message = jsonObject.getJSONObject("message");
                return message.getStr("content");
            } else {
                JSONObject response = JSONUtil.parseObj(httpResponse.body());
                return "请求错误," + response.getStr("message");
            }
        } catch (Exception e) {
            return "请求错误," + e.getMessage();
        }
    }

    /**
     * 同步执行http请求
     * <p>
     * 使用retrofit2
     *
     * @param call retrofit2请求
     * @return 返回消息
     */
    public String execute(Call<ResponseInfo> call) {
        try {
            Response<ResponseInfo> execute = call.execute();
            if (execute.isSuccessful()) {
                ResponseInfo body = execute.body();
                if (body == null) {
                    return "错误:返回结果为空！";
                }
                Log.debug("OpenAi request body ->" + body.toJson());
                return body.getMessage();
            } else {
                try (ResponseBody responseBody = execute.errorBody()) {
                    if (responseBody != null) {
                        String string = responseBody.string();
                        Log.debug("OpenAi request body ->" + string);
                        return JSONUtil.parseObj(string).getJSONObject("error").getStr("message");
                    }
                }
                return execute.message().isEmpty() ? "请求错误:null" : execute.message();
            }
        } catch (IOException e) {
            return "请求错误," + e.getMessage();
        }
    }

    /**
     * 异步发送retrofit2请求
     * 未实现
     *
     * @param call retrofit2请求
     */
    private void asynchronous(Call<ResponseInfo> call) {
        call.enqueue(new Callback<>() {
            /**
             * 已为收到的HTTP响应调用。
             * <p>
             * 注意: HTTP响应仍可能指示应用程序级故障，例如404或500。
             * 呼叫 {@link Response#isSuccessful()} 以确定响应是否指示成功。
             *
             * @param call     响应结果
             * @param response 响应信息
             */
            @Override
            public void onResponse(@NotNull Call<ResponseInfo> call, @NotNull Response<ResponseInfo> response) {

            }

            /**
             * 当与服务器通话时发生网络异常或发生意外时调用
             * 创建请求或处理响应时出现异常。
             *
             * @param call 响应结果
             * @param t    异常
             */
            @Override
            public void onFailure(@NotNull Call<ResponseInfo> call, @NotNull Throwable t) {

            }
        });
    }

}
