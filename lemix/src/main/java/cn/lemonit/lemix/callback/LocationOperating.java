package cn.lemonit.lemix.callback;

import android.app.Activity;
import android.content.Context;

import java.util.Map;

/**
 * @author zhaoguangyang
 * @date 2018/10/30
 * Describe:
 */
public interface LocationOperating {

    /**
     * 单次定位
     * @param locationCallback
     */
    void startOnceLocation(LocationCallback locationCallback);

    /**
     * 连续定位打开
     * @param locationCallback
     */
    void onInstantPosition(LocationCallback locationCallback);

    /**
     * 连续定位关闭
     * @param locationCallback
     */
    void offInstantPosition(LocationCallback locationCallback);

    /**
     * 设置参数
     * @param mMap
     * @return
     */
    LocationOperating setMap(Map<String, Object> mMap);

    /**
     * 设置context
     * @param activity
     * @return
     */
    LocationOperating setContext(Activity activity);
}
