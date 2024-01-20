package me.weishu.kernelsu.net;




import io.reactivex.Observable;
import me.weishu.kernelsu.bean.GetModelResult;
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
    Observable<String> getFile(@Path("path")String path);
}
