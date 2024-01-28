package me.weishu.kernelsu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import me.weishu.kernelsu.bean.HttpResult;
import me.weishu.kernelsu.bean.LoginResult;
import me.weishu.kernelsu.databinding.LoginActivityBinding;

import me.weishu.kernelsu.utils.SpUtils;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityBinding inflate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflate = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());
        startActivity(new Intent(LoginActivity.this, me.weishu.kernelsu.ui.MainActivity.class));
        String token = SpUtils.getInstance().getString("token", "");
        if (!TextUtils.isEmpty(token)) {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
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
//        Disposable subscribe = CommonRetrofitManager.getInstance().login(strCode).subscribe(new Consumer<LoginResult>() {
//            @Override
//            public void accept(LoginResult loginResult) throws Exception {
//
//                if (loginResult.getCode() == 1) {
//                    String token = loginResult.getData().getToken();
////                    Settings.System.putString(LoginActivity.this.getContentResolver(), "token", token);
//                    setToken(token);
//                    SpUtils.getInstance().putString("token", token);
//
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
//                } else {
//                    Toast.makeText(LoginActivity.this, "激活失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//                throwable.printStackTrace();
//                Toast.makeText(LoginActivity.this, "激活失败", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void setToken(String token) {
//        CommonRetrofitManager.getInstance().setToken(token).subscribe(new Consumer<HttpResult>() {
//            @Override
//            public void accept(HttpResult result) throws Exception {
//
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//
//            }
//        });
    }

}
