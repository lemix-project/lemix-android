package cn.lemonit.lemix.mixmodule;


import android.app.Activity;
import android.app.ActivityManager;

import java.util.List;

import cn.lemonit.lemix.callback.MixModuleLifeCycle;

/**
 * 插件生命周期回调和插件同一管理
 */
public class MixModuleInstance {

    private MixModuleLifeCycle moduleLifeCycle;

    private MixModuleInfo moduleInfo;

    private ActivityManager.RunningTaskInfo uiStackObj;

    private List<Activity> taskActivityList;

    public MixModuleLifeCycle getModuleLifeCycle() {
        return moduleLifeCycle;
    }

    public MixModuleInstance setModuleLifeCycle(MixModuleLifeCycle moduleLifeCycle) {
        this.moduleLifeCycle = moduleLifeCycle;
        return this;
    }

    public MixModuleInfo getModuleInfo() {
        return moduleInfo;
    }

    public MixModuleInstance setModuleInfo(MixModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
        return this;
    }

    public ActivityManager.RunningTaskInfo getUiStackObj() {
        return uiStackObj;
    }

    public void setUiStackObj(ActivityManager.RunningTaskInfo uiStackObj) {
        this.uiStackObj = uiStackObj;
    }

    public List<Activity> getTaskActivityList() {
        return taskActivityList;
    }

    public void setTaskActivityList(List<Activity> taskActivityList) {
        this.taskActivityList = taskActivityList;
    }
}
