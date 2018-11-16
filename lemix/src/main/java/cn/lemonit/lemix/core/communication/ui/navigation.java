package cn.lemonit.lemix.core.communication.ui;

import cn.lemonit.lemix.core.operate.ui.UiNavigation;
import cn.lemonit.lemix.module.AimActivityInfo;
import cn.lemonit.lemix.module.CommunicationInfo;

public class navigation {

    /**
     * 从右往左推出新界面
     * @param communicationInfo
     */
    public void push(CommunicationInfo communicationInfo) {
        AimActivityInfo aimActivityInfo = new AimActivityInfo();
        aimActivityInfo.setAim((String) communicationInfo.getParams().get("aim"));
        aimActivityInfo.setType((String) communicationInfo.getParams().get("type"));
        new UiNavigation().push(communicationInfo.getActivity(), aimActivityInfo,  communicationInfo.getParams());
    }

    /**
     * 从下往上弹出新界面
     * @param communicationInfo
     */
    public void present(CommunicationInfo communicationInfo) {
        AimActivityInfo aimActivityInfo = new AimActivityInfo();
        aimActivityInfo.setAim((String) communicationInfo.getParams().get("aim"));
        aimActivityInfo.setType((String) communicationInfo.getParams().get("type"));
//        new UiNavigation().present(communicationInfo.getActivity(), aimActivityInfo, communicationInfo.getParams());
    }

    /**
     * 怎么进来的界面就怎么返回
     * @param communicationInfo
     */
    public void pop(CommunicationInfo communicationInfo) {
        int count = communicationInfo.getParams().size() == 0 ? 1 : Integer.parseInt((String) communicationInfo.getParams().get("layer"));
//        new UiNavigation().pop(communicationInfo.getActivity(), count);
    }

    /**
     * 关闭相同的二维的界面（无论几层）
     * @param communicationInfo
     */
    public void close(CommunicationInfo communicationInfo) {
        int count = communicationInfo.getParams().size() == 0 ? 1 : Integer.parseInt((String) communicationInfo.getParams().get("layer"));
//        new UiNavigation().close(communicationInfo.getActivity(), count);
    }
}
