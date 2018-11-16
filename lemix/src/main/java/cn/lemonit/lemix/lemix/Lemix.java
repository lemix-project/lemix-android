package cn.lemonit.lemix.lemix;

import android.app.Application;

/**
 * 插件引擎工厂类，只是创建插件引擎作用
 */
public class Lemix {

    private static LemixEngineConfig defaultConfig;
    private static LemixEngine defaultEngine;
    private static Application mApplication;
    private static final String DEFAULT_ENGINE_NAME = "default";

    /**
     * Lemix启动器，在此完成Lemix的初始化信息相关工作。必须在使用Lemix任意功能前调用。放到App启动时调用。(Application中完成引擎初始化)
     * @param config
     * @param application
     */
    public static void startWork(LemixEngineConfig config, Application application) {
        mApplication = application;
        defaultConfig = config;
    }

    /**
     * 获取默认的引擎
     * @return
     */
    public static LemixEngine defaultEngine() {
        if(defaultEngine == null) {
            defaultEngine = createEngine(DEFAULT_ENGINE_NAME, defaultConfig, mApplication);
        }
        return defaultEngine;
    }

    /**
     * 获取自定义的引擎
     * @param lemixEngineName
     * @param config
     * @return
     */
    public static LemixEngine createEngine(String lemixEngineName, LemixEngineConfig config, Application application) {
        LemixEngine lemixEngine = new LemixEngine(lemixEngineName, config, application);
        return lemixEngine;
    }
}
