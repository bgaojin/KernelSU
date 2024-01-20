package me.weishu.kernelsu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Consumer;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.weishu.kernelsu.bean.HttpResult;
import me.weishu.kernelsu.bean.LoginResult;
import me.weishu.kernelsu.databinding.LoginActivityBinding;
import me.weishu.kernelsu.net.CommonRetrofitManager;
import me.weishu.kernelsu.net.HttpUtils;
import me.weishu.kernelsu.utils.SpUtils;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityBinding inflate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflate = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());

        String token = SpUtils.getInstance().getString("token", "");
        if (!TextUtils.isEmpty(token)) {
            String url = "http://127.0.0.1:1991/app/v1/setToken?token="+token;
            HttpUtils.requestGet(url, new HttpUtils.RequestListener() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("requestGet="+result);
                }

                @Override
                public void onFailed() {

                }
            });
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        inflate.llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String strCode = inflate.etLoginAccount.getText().toString().trim();
        if (TextUtils.isEmpty(strCode)) {
            Toast.makeText(this, "请输入激活码", Toast.LENGTH_SHORT).show();
            return;
        }
        Disposable subscribe = CommonRetrofitManager.getInstance().login(strCode).subscribe(new Consumer<LoginResult>() {
            @Override
            public void accept(LoginResult loginResult) throws Exception {
//9caea01ce00412a3c60bfe65ec89ae39
                if (loginResult.getCode() == 1) {
                    String token = loginResult.getData().getToken();
                    String url = "http://127.0.0.1:1991/app/v1/setToken?token="+token;

                    HttpUtils.requestGet(url, new HttpUtils.RequestListener() {
                        @Override
                        public void onSuccess(String result) {
                            System.out.println("requestGet="+result);
                        }

                        @Override
                        public void onFailed() {

                        }
                    });

                    SpUtils.getInstance().putString("token", token);
//                    Settings.System.putString(LoginActivity.this.getContentResolver(), "token", token);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "激活失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                Toast.makeText(LoginActivity.this, "激活失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
