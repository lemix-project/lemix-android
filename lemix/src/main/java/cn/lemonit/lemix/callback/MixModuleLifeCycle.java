package cn.lemonit.lemix.callback;

/**
 * 生命周期回调
 */
public interface MixModuleLifeCycle {

    /**
     * 插件显示的时候
     */
    void onShow(String pluginName);

    /**
     * 插件隐藏的时候
     */
    void onHide(String pluginName);

    /**
     * 开始下载插件的时候
     */
    void onLoad(String pluginName);

    /**
     * 插件关闭的时候
     */
    void onClose(String pluginName);
}
