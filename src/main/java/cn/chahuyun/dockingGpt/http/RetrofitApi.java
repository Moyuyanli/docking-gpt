package cn.chahuyun.dockingGpt.http;

import cn.chahuyun.dockingGpt.constant.Constant;
import cn.chahuyun.dockingGpt.entity.RequestInfo;
import cn.chahuyun.dockingGpt.entity.ResponseInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * http请求api
 *
 * @author Moyuyanli
 * @date 2023/10/19 9:51
 */
public interface RetrofitApi {

    /**
     * 向ai接口发送请求
     *
     * @param token       token
     * @param requestInfo 请求信息
     * @return retrofit2.Call<okhttp3.ResponseBody>
     * @author Moyuyanli
     * @date 2023/10/19 14:10
     */
    @POST(Constant.REQUEST_URL_SUFFIX)
    Call<ResponseInfo> question(@Header("Authorization") String token, @Body RequestInfo requestInfo);


}
