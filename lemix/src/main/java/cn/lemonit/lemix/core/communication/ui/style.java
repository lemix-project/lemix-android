package cn.lemonit.lemix.core.communication.ui;

import cn.lemonit.lemix.core.operate.ui.UiStyle;
import cn.lemonit.lemix.module.CommunicationInfo;

/**
 * JS的执行原生的入口
 */
public class style {

    /**
     * 是否显示标题栏
     * @param communicationInfo
     */
    public void setNavigationBarHidden(CommunicationInfo communicationInfo) {
        new UiStyle().setNavigationBarHidden(communicationInfo.getActivity(), (Boolean) communicationInfo.getParams().get("isHidden"));
    }

    /**
     * 设置主题颜色（暂时显示的是标题栏颜色）
     * @param communicationInfo
     */
    public void setNavigationBackgroundColor(CommunicationInfo communicationInfo) {
        new UiStyle().setNavigationBackgroundColor(communicationInfo.getActivity(), (String) communicationInfo.getParams().get("color"));
    }

    /**
     * 设置标题栏标题
     * @param communicationInfo
     */
    public void setNavigationTitle(CommunicationInfo communicationInfo) {
        new UiStyle().setNavigationTitle(communicationInfo.getActivity(), (String) communicationInfo.getParams().get("title"));
    }

    /**
     * 设置状态栏是dark还是light
     * @param communicationInfo
     */
    public void setStatusBarStyle(CommunicationInfo communicationInfo) {
        new UiStyle().setStatusBarStyle(communicationInfo.getActivity(), (String) communicationInfo.getParams().get("style"));
    }

    /**
     * 设置状态栏是否隐藏
     * @param communicationInfo
     */
    public void setStatusBarHidden(CommunicationInfo communicationInfo) {
        new UiStyle().setStatusBarHidden(communicationInfo.getActivity(), (Boolean) communicationInfo.getParams().get("isHidden"));
    }

}
