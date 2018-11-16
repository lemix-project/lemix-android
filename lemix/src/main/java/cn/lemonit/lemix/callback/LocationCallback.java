package cn.lemonit.lemix.callback;

/**
 * @author zhaoguangyang
 * @date 2018/10/30
 * Describe: 定位结果回调
 */
public interface LocationCallback {

    void getLocation(final String lon, final String lat, final String address, final String result);
}
