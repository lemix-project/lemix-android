package cn.lemonit.lemix.mixmodule;

/**
 * 原生界面的信息配置接口
 */
public class NativePageInfo {
    /**
     * key
     */
    private String nativePageKey;
    /**
     * 具体的原生界面类
     */
    private Class nativePage;

    public String getNativePageKey() {
        return nativePageKey;
    }

    public void setNativePageKey(String nativePageKey) {
        this.nativePageKey = nativePageKey;
    }

    public Class getNativePage() {
        return nativePage;
    }

    public void setNativePage(Class nativePage) {
        this.nativePage = nativePage;
    }
}
