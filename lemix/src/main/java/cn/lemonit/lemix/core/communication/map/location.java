package cn.lemonit.lemix.core.communication.map;

import cn.lemonit.lemix.WebActivity;
import cn.lemonit.lemix.callback.LocationOperating;
import cn.lemonit.lemix.core.operate.map.MapLocation;
import cn.lemonit.lemix.lemix.Lemix;
import cn.lemonit.lemix.lemix.LemixEngine;
import cn.lemonit.lemix.module.CommunicationInfo;

/**
 * JS端定位执行入口
 */
public class location {

    /**
     * 一次定位
     * @param communicationInfo
     */
    public void getPosition(CommunicationInfo communicationInfo) {
        LocationOperating locationOperating = Lemix.defaultEngine().getLocationOperating().setMap(communicationInfo.getParams()).setContext((WebActivity) communicationInfo.getActivity());
        MapLocation.getInstance().setLocationOperating(locationOperating).startOnceLocation((WebActivity) communicationInfo.getActivity());
    }

    /**
     * 连续定位打开
     * @param communicationInfo
     */
    public void onInstantPosition(CommunicationInfo communicationInfo) {
        LocationOperating locationOperating = Lemix.defaultEngine().getLocationOperating().setMap(communicationInfo.getParams()).setContext((WebActivity) communicationInfo.getActivity());
        MapLocation.getInstance().setLocationOperating(locationOperating).onInstantPosition((WebActivity) communicationInfo.getActivity());
    }

    /**
     * 连续定位关闭
     * @param communicationInfo
     */
    public void offInstantPosition(CommunicationInfo communicationInfo) {
        LocationOperating locationOperating = Lemix.defaultEngine().getLocationOperating().setMap(communicationInfo.getParams()).setContext((WebActivity) communicationInfo.getActivity());
        MapLocation.getInstance().setLocationOperating(locationOperating).offInstantPosition((WebActivity) communicationInfo.getActivity());
    }
}
