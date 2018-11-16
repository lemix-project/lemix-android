package cn.lemonit.lemix.module;

import java.util.Map;

import cn.lemonit.lemix.base.BaseActivity;

public class CommunicationInfo {

    private BaseActivity activity;
    private Map<String, Object> params;
    private String type;

    public CommunicationInfo() {

    }

    public CommunicationInfo(BaseActivity activity, Map<String, Object> data) {
        this.activity = activity;
        this.params = (Map<String, Object>) data.get("params");
        this.type = (String) data.get("type");
    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BaseActivity getActivity() {
        return activity;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String getType() {
        return type;
    }
}
