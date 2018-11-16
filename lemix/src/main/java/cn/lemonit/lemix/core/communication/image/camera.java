package cn.lemonit.lemix.core.communication.image;

import android.util.Log;

import cn.lemonit.lemix.WebActivity;
import cn.lemonit.lemix.core.operate.image.ImageCamera;
import cn.lemonit.lemix.module.CommunicationInfo;


/**
 * 拍照入口
 */
public class camera {

    private String TAG = "camera";

    public void open(CommunicationInfo communicationInfo) {
        Log.e(TAG, "mode == " + communicationInfo.getParams().get("mode"));
        Log.e(TAG, "maxLength == " + communicationInfo.getParams().get("maxLength"));
        Log.e(TAG, "success == " + communicationInfo.getParams().get("success"));
        Log.e(TAG, "failed == " + communicationInfo.getParams().get("failed"));
        new ImageCamera().open((WebActivity) communicationInfo.getActivity(), communicationInfo.getParams());
    }
}
