package cn.chahuyun.docking.http

import cn.chahuyun.docking.RequestParams
import cn.chahuyun.docking.entity.RequestInfo
import cn.chahuyun.docking.entity.ResponseInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * http请求api
 *
 * @author Moyuyanli
 * @date 2023/10/19 9:51
 */
interface RetrofitApi {
    /**
     * 向ai接口发送请求
     *
     * @param token       token
     * @param requestInfo 请求信息
     * @return retrofit2.Call<okhttp3.ResponseBody>
     * @author Moyuyanli
     * @date 2023/10/19 14:10
    </okhttp3.ResponseBody> */
    @POST(RequestParams.REQUEST_URL_SUFFIX)
    fun question(@Header("Authorization") token: String, @Body requestInfo: RequestInfo): Call<ResponseInfo?>
}