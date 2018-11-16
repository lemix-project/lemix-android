package cn.lemonit.lemix.core.operate.image;

import android.text.TextUtils;
import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.lemonit.lemage.Lemage;
import cn.lemonit.lemage.interfaces.LemageCameraCallback;
import cn.lemonit.lemix.WebActivity;

/**
 * 真正执行入口
 */
public class ImageCamera {

    private String TAG = "ImageCamera";

    /**
     * 拍照或者录像
     */
    public void open(final WebActivity context, final Map<String, Object> map) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Lemage.startCamera(context, (int) Math.floor((Double) map.get("maxLength")), new LemageCameraCallback() {
                    @Override
                    public void cameraActionFinish(List<String> list) {
                        for(String str : list) {
                            Log.e(TAG, "str == " + str);
                            if(!TextUtils.isEmpty(str)) {
                                context.loadCallback(str, (String) map.get("success"));
                            }else {
                                context.loadCallback(str, (String) map.get("failed"));
                            }
                        }
                    }
                });
            }
        });
    }
}
