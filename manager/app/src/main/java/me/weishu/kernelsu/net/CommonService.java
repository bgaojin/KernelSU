package me.weishu.kernelsu.net;


import io.reactivex.Observable;
import me.weishu.kernelsu.bean.GetModelResult;
import me.weishu.kernelsu.bean.HttpResult;
import me.weishu.kernelsu.bean.LoginResult;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * @author: ji xin
 * @date : 2020/7/1下午12:33
 * @desc :
 */
public interface CommonService {

    @GET("/api/device/active")
    Observable<LoginResult> login(@Query("code_no") String code_no);

    @GET("/api/file/getfile")
    Observable<GetModelResult> getModel(@Query("token") String token, @Query("cate") String cate);

    @GET("/uploads/{path}")
    Observable<String> getFile(@Path("path") String path);

    @GET("/app/v1/resetApp")
    Observable<HttpResult> restApp(@Query("destPackageInfos") String destPackageInfos, @Query("fileID") String fileID);

    @GET("/app/v1/modifyPhone")
    Observable<HttpResult> modifyPhone(@Query("destPackageInfos") String destPackageInfos, @Query("sdkVersion") String sdkVersion);

    @GET("/app/v1/startVpn")
    Observable<HttpResult> startVpn(@Query("ip") String ip, @Query("port") String port, @Query("userName") String userName, @Query("pwd") String pwd);

    @GET("/app/v1/closeVpn")
    Observable<HttpResult> closeVpn();

    @GET("/app/v1/backUpApp")
    Observable<HttpResult> backUpApp(@Query("destPackageInfos") String destPackageInfos);

    @GET("/app/v1/setToken")
    Observable<HttpResult> setToken(@Query("token") String token);
}
