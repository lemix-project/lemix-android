package cn.lemonit.lemix.core.communication.net;

import java.util.Map;

import cn.lemonit.lemix.WebActivity;
import cn.lemonit.lemix.core.operate.net.NetHttp;
import cn.lemonit.lemix.module.CommunicationInfo;

/**
 * 网络请求JS入口
 */
public class http {

    /**
     * 同步请求
     */
    public Map syncRequest(CommunicationInfo communicationInfo) {
        return new NetHttp().syncRequest((WebActivity) communicationInfo.getActivity(), communicationInfo.getParams());
    }

    /**
     * 异步请求
     */
    public void asyncRequest(CommunicationInfo communicationInfo) {
        new NetHttp().asyncRequest((WebActivity) communicationInfo.getActivity(), communicationInfo.getParams());
    }

    /**
     * 上传
     */
    public void uploadFile(CommunicationInfo communicationInfo) {
        new NetHttp().uploadFile((WebActivity) communicationInfo.getActivity(), communicationInfo.getParams());
    }

    /**
     * 下载
     */
    public void downloadFile(CommunicationInfo communicationInfo) {
        new NetHttp().downloadFile((WebActivity) communicationInfo.getActivity(), communicationInfo.getParams());
    }
}
