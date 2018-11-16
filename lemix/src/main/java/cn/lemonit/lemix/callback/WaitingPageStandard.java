package cn.lemonit.lemix.callback;


import cn.lemonit.lemix.mixmodule.MixModuleInfo;

/**
 * 等待页的回调
 * 当引擎下载，解压，扫描时回调给等待页
 */
public interface WaitingPageStandard {

    /**
     * 当引擎执行startUp刚启动等待页时启动，把要启动的插件信息传递给等待页，如：插件名，插件图标等，由调用者自行显示设计
     * @param moduleInfo
     */
    void onWaiting(MixModuleInfo moduleInfo);

    /**
     * 下载插件时回调
     */
    void onLoad();
    /**
     * 解压插件时
     */
    void onUnZip();

    /**
     * 扫描插件时
     */
    void onScan();
}
