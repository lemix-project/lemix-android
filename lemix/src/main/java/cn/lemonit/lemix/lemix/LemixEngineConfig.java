package cn.lemonit.lemix.lemix;

import android.app.Activity;
import android.util.Log;

import cn.lemonit.lemix.base.BaseActivity;
import cn.lemonit.lemix.callback.LocationOperating;
import cn.lemonit.lemix.callback.WaitingPageStandard;
import cn.lemonit.lemix.exception.ActivityInvalidException;

/**
 * 引擎配置类
 */
public class LemixEngineConfig {

    /**
     * 存放LemixEngine运行时产生的需要永久存储的相关文件，如远程MixModule包的缓存等
     */
    private String workspacePath;
    /**
     * 存放LemixEngine运行时产生的临时数据，如MixModule解压后数据，临时缓存等
     */
    private String tempPath;
    /**
     * 等待页, 由调用者传入
     */
    private Class waitingPage;
    /**
     * 定位的实现类
     */
    private LocationOperating mLocationOperating;

    public LemixEngineConfig(String workspacePath, String tempPath) {
        this.workspacePath = workspacePath;
        this.tempPath = tempPath;
    }

    public String getWorkspacePath() {
        return workspacePath;
    }

    public String getTempPath() {
        return tempPath;
    }

    public LemixEngineConfig setWorkspacePath(String workspacePath) {
        this.workspacePath = workspacePath;
        return this;
    }

    public LemixEngineConfig setTempPath(String tempPath) {
        this.tempPath = tempPath;
        return this;
    }

    public Class getWaitPager() {
        return waitingPage;
    }

    public void setWaitPager(Class waitingPage) throws ActivityInvalidException {
        // 判断类waitPager是否是WaitingPageStandard
        Log.e("activity", "WaitingPageStandard ======== " + !WaitingPageStandard.class.isAssignableFrom(waitingPage));
        Log.e("activity", "BaseActivity ======== " + !BaseActivity.class.isAssignableFrom(waitingPage));
//        if(!WaitingPageStandard.class.isAssignableFrom(waitingPage) || !BaseActivity.class.isAssignableFrom(waitingPage)) {
        if(!BaseActivity.class.isAssignableFrom(waitingPage)) {
            throw new ActivityInvalidException(waitingPage.getName());
        }else {
            this.waitingPage = waitingPage;
        }
    }

    public void setLocationOperating(LocationOperating mLocationOperating) {
        this.mLocationOperating = mLocationOperating;
    }

    public LocationOperating getLocationOperating() {
        return mLocationOperating;
    }
}
