package me.weishu.kernelsu.net;

import io.reactivex.Observable;
import me.weishu.kernelsu.bean.GetModelResult;
import me.weishu.kernelsu.bean.HttpResult;
import me.weishu.kernelsu.bean.LoginResult;

/**
 * @author: ji xin
 * @date : 2020/7/1下午12:33
 * @desc :
 */
public class CommonRetrofitManager extends BaseRetrofitManager {
    private CommonService mCommonService;

    public CommonRetrofitManager() {
        //构造函数获取retrofit对象，并创建接口类的对象，拿到这个对象，可以调用具体的接口
        mCommonService = getRetrofit().create(CommonService.class);
    }

    private static class SingletonHolder {
        private static final CommonRetrofitManager INSTANCE = new CommonRetrofitManager();
    }

    public static CommonRetrofitManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Observable<LoginResult> login(String code_no) {
        return mCommonService.login(code_no).compose(RxSchedulers.applySchedulers());
    }

    public Observable<GetModelResult> getModel(String token) {
        return mCommonService.getModel(token, "Models").compose(RxSchedulers.applySchedulers());
    }

    public Observable<String> getFile(String path) {
        return mCommonService.getFile(path).compose(RxSchedulers.applySchedulers());
    }

    public Observable<HttpResult> restApp(String pkgName, String fileName) {
        return mCommonService.restApp(pkgName, fileName).compose(RxSchedulers.applySchedulers());
    }

    public Observable<HttpResult> modifyPhone(String pkgName, String sdkVersion, String sdkInt) {
        return mCommonService.modifyPhone(pkgName, sdkVersion, sdkInt).compose(RxSchedulers.applySchedulers());
    }

    public Observable<HttpResult> startVpn(String ip, String port, String userName, String pwd) {
        return mCommonService.startVpn(ip, port, userName, pwd).compose(RxSchedulers.applySchedulers());
    }

    public Observable<HttpResult> closeVpn() {
        return mCommonService.closeVpn().compose(RxSchedulers.applySchedulers());
    }

    public Observable<HttpResult> backUpApp(String pkgName) {
        return mCommonService.backUpApp(pkgName).compose(RxSchedulers.applySchedulers());
    }

    public Observable<HttpResult> setToken(String token) {
        return mCommonService.setToken(token).compose(RxSchedulers.applySchedulers());
    }
}
