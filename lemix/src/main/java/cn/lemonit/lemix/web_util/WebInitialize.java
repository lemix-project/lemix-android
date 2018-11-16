package cn.lemonit.lemix.web_util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

public class WebInitialize {

    public static void X5Initialize(final Context context) {

        QbSdk.PreInitCallback callback = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
//                Log.e("ZWTApplication", "onCoreInitFinished");
            }

            @Override
            public void onViewInitFinished(boolean b) {
//                Log.e("ZWTWebActivity", "onViewInitFinished 结果 === " + b);

            }
        };
        QbSdk.initX5Environment(context, callback);

        QbSdk.setTbsListener(new TbsListener() {

            // 腾讯X5内核 下载结束
            @Override
            public void onDownloadFinish(int i) {
//                Log.e("ZWTApplication", "onDownloadFinish is " + i);
                if(i == 100) {
                    Toast.makeText(context, "X5内核加载结束", Toast.LENGTH_SHORT).show();
                }
            }

            // 腾讯X5内核 安装完成
            @Override
            public void onInstallFinish(int i) {
//                Log.e("ZWTApplication", "onInstallFinish is " + i);
            }

            // 下载进度
            @Override
            public void onDownloadProgress(int i) {
//                Log.e("ZWTApplication", "onDownloadProgress:" + i);
            }
        });
    }
}
