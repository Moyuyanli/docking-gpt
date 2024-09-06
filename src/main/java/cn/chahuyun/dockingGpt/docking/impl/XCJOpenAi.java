package cn.chahuyun.dockingGpt.docking.impl;

import cn.chahuyun.dockingGpt.constant.Constant;
import cn.chahuyun.dockingGpt.docking.AbstractRequest;
import cn.chahuyun.dockingGpt.entity.ProxyInfo;
import cn.chahuyun.dockingGpt.entity.RecordMessageInfo;
import cn.chahuyun.dockingGpt.entity.ResponseInfo;
import cn.chahuyun.dockingGpt.http.RetrofitApi;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * 使用小纯洁openAi地址
 *
 * @author Moyuyanli
 * @Date 2023/7/30 1:13
 */
@Setter
@Getter
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class XCJOpenAi extends AbstractRequest {

    private String url = Constant.XCJ_AI_URL_PREFIX;

    public XCJOpenAi(String aiKey, String setAi, double temperature, String model, ProxyInfo proxyInfo) {
        super(aiKey, setAi, temperature, model, proxyInfo);
    }

    /**
     * 暂时的一个可以实现功能的实现方法
     *
     * @param info 消息信息
     * @return 回答消息
     */
    @Override
    public String msgRequest(RecordMessageInfo info) {
        int timeout = 30;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<ResponseInfo> question = retrofitApi.question(getAuthorizeToken(), getParams(info.getMessage()));

        return execute(question);
    }
}
