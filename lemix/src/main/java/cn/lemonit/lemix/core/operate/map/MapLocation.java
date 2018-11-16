package cn.lemonit.lemix.core.operate.map;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;

import cn.lemonit.lemix.WebActivity;
import cn.lemonit.lemix.callback.LocationCallback;
import cn.lemonit.lemix.callback.LocationOperating;

/**
 * 定位真正执行入口
 */
public class MapLocation implements LocationCallback {

    private LocationOperating mLocationOperating;
    private WebActivity mContext;

    private MapLocation(){}
    private static MapLocation instance;

    public static MapLocation getInstance() {
        if(instance == null) {
            instance = new MapLocation();
        }
        return instance;
    }

    /**
     * 开始一次定位
     * @param context
     */
    public void startOnceLocation(WebActivity context) {
        mContext = context;
        mLocationOperating.startOnceLocation(this);
    }

    /**
     * 开始连续定位
     * @param context
     */
    public void onInstantPosition(WebActivity context) {
        mContext = mContext;
        mLocationOperating.onInstantPosition(this);
    }

    /**
     * 关闭连续定位
     * @param context
     */
    public void offInstantPosition(WebActivity context) {
        mContext = context;
        mLocationOperating.offInstantPosition(this);
    }

    public MapLocation setLocationOperating(LocationOperating locationOperating) {
        mLocationOperating = locationOperating;
        return this;
    }

    @Override
    public void getLocation(String lon, String lat, String address, String result) {
        mContext.getLocation(lon, lat, address, result);
        Log.e("Activity", "MapLocation 定位结果 == " + "lon: " + lon + "  lat: " + lat + "  address: " + address + "  result: " + result);
    }

}
