package me.weishu.kernelsu.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import io.reactivex.functions.Consumer;
import me.weishu.kernelsu.R;
import me.weishu.kernelsu.bean.HttpResult;
import me.weishu.kernelsu.databinding.ActivityVpnSetBinding;

import me.weishu.kernelsu.net.CommonRetrofitManager;
import me.weishu.kernelsu.utils.ApiUtils;
//import me.weishu.kernelsu.utils.GsonUtils;
import me.weishu.kernelsu.utils.SpUtils;
import me.weishu.kernelsu.utils.VpnUtils;

public class VpnSetActivity extends AppCompatActivity {

    private me.weishu.kernelsu.databinding.ActivityVpnSetBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVpnSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.tvTitle.title.setText("VPN设置");
        String host = SpUtils.getInstance().getString("host", "");
        String port = SpUtils.getInstance().getString("port", "");
        String userName = SpUtils.getInstance().getString("userName", "");
        String pwd = SpUtils.getInstance().getString("pwd", "");
        binding.ip.setText(host);
        binding.port.setText(port);
        binding.userName.setText(userName);
        binding.pwd.setText(pwd);
        ProgressDialog dialog = new ProgressDialog(this);
        binding.startVpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = binding.ip.getText().toString().trim();
                String port = binding.port.getText().toString().trim();
                String userName = binding.userName.getText().toString().trim();
                String pwd = binding.pwd.getText().toString().trim();

                if (TextUtils.isEmpty(ip)) {
                    return;
                }

                if (TextUtils.isEmpty(port)) {
                    return;
                }

                if (TextUtils.isEmpty(userName)) {
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    return;
                }
                dialog.setMessage("开启中...");
                if (!dialog.isShowing()) {
                    dialog.show();
                }


                CommonRetrofitManager.getInstance().startVpn(ip, port, userName, pwd).subscribe(new Consumer<HttpResult>() {
                    @Override
                    public void accept(HttpResult result) throws Exception {
                        if ("1".equals(result.getRet())) {
                            SpUtils.getInstance().putString("host",ip);
                            SpUtils.getInstance().putString("port",port);
                            SpUtils.getInstance().putString("userName",userName);
                            SpUtils.getInstance().putString("pwd",pwd);
                            showMsg("开启成功");
                        } else {
                            showMsg("开启失败");
                        }
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        showMsg("开启失败");
                        dialog.dismiss();
                    }
                });
//
            }
        });

        binding.closeVpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!VpnUtils.hasVpn(VpnSetActivity.this)){
//                    return;
//                }
                dialog.setMessage("关闭中...");
                if (!dialog.isShowing()) {
                    dialog.show();
                }

                CommonRetrofitManager.getInstance().closeVpn().subscribe(new Consumer<HttpResult>() {
                    @Override
                    public void accept(HttpResult result) throws Exception {
                        if ("1".equals(result.getRet())) {
                            showMsg("关闭成功");
                        } else {
                            showMsg("关闭失败");
                        }
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        showMsg("关闭失败");
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
