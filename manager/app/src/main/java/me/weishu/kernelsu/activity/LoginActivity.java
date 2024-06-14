package me.weishu.kernelsu.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.weishu.kernelsu.Natives;
import me.weishu.kernelsu.bean.HttpResult;
import me.weishu.kernelsu.bean.LoginResult;
import me.weishu.kernelsu.databinding.LoginActivityBinding;

import me.weishu.kernelsu.net.BaseRetrofitManager;
import me.weishu.kernelsu.net.CommonRetrofitManager;
import me.weishu.kernelsu.ui.util.KsuCliKt;
import me.weishu.kernelsu.utils.SpUtils;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityBinding inflate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflate = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());
        boolean b = Natives.INSTANCE.becomeManager(getPackageName());
        setVolume();
        if (b) {
            KsuCliKt.install();
        }

        String packageName = "com.android.ghost.service";

        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(packageName, 0);
            Natives.Profile profile = Natives.INSTANCE.getAppProfile(packageName, applicationInfo.uid);

            if (!profile.getAllowSu()) {
                Natives.Profile copy = profile.copy(packageName, profile.getCurrentUid(),
                        true, true, null,
                        0, 0, new ArrayList<>(), new ArrayList<>(),
                        "u:r:su:s0", 0, true,
                        true, "");
                Natives.INSTANCE.setAppProfile(copy);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        String token = SpUtils.getInstance().getString("token", "");

//        不需要激活验证
//          token = "4e3ae5a9cb23f38775ceb185e1fc7f2a";
        if (!TextUtils.isEmpty(token)) {
            Settings.System.putString(getContentResolver(), "token", token);
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
    public boolean hasSu() {
        Process process;
        try {
            process = Runtime.getRuntime().exec(new String[]{"su", "-c", "echo"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            return line != null && line.contains("root");
        } catch (IOException e) {
            return false;
        }
    }
    private void setVolume() {
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        amanager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
        amanager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
        amanager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        amanager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
        amanager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);

        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        amanager.setStreamMute(AudioManager.STREAM_RING, true);
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
    }


    private void login() {
        String wcpPath = Settings.System.getString(getContentResolver(), "wcp_path");

        if (TextUtils.isEmpty(wcpPath)) {
            wcpPath = "http://101.42.2.49:8081";
            Settings.System.putString(getContentResolver(), "wcp_path", wcpPath);
        }
//        wcpPath = "http://101.42.2.49:8081";
        BaseRetrofitManager.BASE_URL = wcpPath;
        String strCode = inflate.etLoginAccount.getText().toString().trim();
        if (TextUtils.isEmpty(strCode)) {
            Toast.makeText(this, "请输入激活码", Toast.LENGTH_SHORT).show();
            return;
        }
        Disposable subscribe = CommonRetrofitManager.getInstance().login(strCode).subscribe(new Consumer<LoginResult>() {
            @Override
            public void accept(LoginResult loginResult) throws Exception {
                System.out.println(loginResult.toString());
                if (loginResult.getCode() == 0) {
                    String token = loginResult.getData().getToken();

                    Settings.System.putString(LoginActivity.this.getContentResolver(), "token", token);
//                    setToken(token);
                    SpUtils.getInstance().putString("token", token);

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

    private void setToken(String token) {
        CommonRetrofitManager.getInstance().setToken(token).subscribe(new Consumer<HttpResult>() {
            @Override
            public void accept(HttpResult result) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }

}
