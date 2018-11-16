package cn.lemonit.lemix.core.operate.net;

import android.text.TextUtils;

import java.util.Map;

import cn.lemonit.lemix.WebActivity;
import cn.lemonit.lemix.util.NetUtil;

/**
 * 原生实际执行入口
 */
public class NetHttp {

    /**
     * 同步请求
     */
    public Map syncRequest(WebActivity activity, Map<String, Object> map) {
        String method = (String) map.get("method");
        if(TextUtils.isEmpty(method)) {
            return null;
        }
        if(method.equals("GET")) {
            return new NetUtil().syncRequestGet(activity, map);
        }else if(method.equals("POST")) {
            return new NetUtil().syncRequestPost(activity, map);
        }
        return null;
    }

    /**
     * 异步请求
     */
    public void asyncRequest(WebActivity activity, Map<String, Object> map) {
        String method = (String) map.get("method");
        if(TextUtils.isEmpty(method)) {
            return;
        }
        if(method.equals("GET")) {
            new NetUtil().asyncRequestGet(activity, map);
        }else if(method.equals("POST")) {
            new NetUtil().asyncRequestPost(activity, map);
        }
    }

    /**
     * 上传
     */
    public void uploadFile(WebActivity activity, Map<String, Object> map) {
        new NetUtil().uploadFile(activity, map);
    }

    /**
     * 下载
     */
    public void downloadFile(WebActivity activity, Map<String, Object> map) {
        new NetUtil().downloadFile(activity, map);
    }
}
