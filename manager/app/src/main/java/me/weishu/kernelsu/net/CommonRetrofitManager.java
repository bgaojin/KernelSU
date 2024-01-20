package me.weishu.kernelsu.net;

import io.reactivex.Observable;
import me.weishu.kernelsu.bean.GetModelResult;
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
        return mCommonService.getModel(token,"Models").compose(RxSchedulers.applySchedulers());
    }

    public Observable<String> getFile(String path) {
        return mCommonService.getFile(path).compose(RxSchedulers.applySchedulers());
    }
}
