package cn.lemonit.lemix.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局记录插件
 */
public class PluginManager {

    private Map<String, Map<String, String>> pluginMap = new HashMap<String, Map<String, String>>();

    private PluginManager() {
    }

    /**
     * 记录当前是哪个插件
     */
    private String pluginName;

    private static PluginManager instance;

    public static synchronized PluginManager getPluginManager() {
        if(instance == null) {
            instance = new PluginManager();
        }
        return instance;
    }

    public Map<String, Map<String, String>> getPluginMap() {
        return pluginMap;
    }

    public void setPluginMap(Map<String, Map<String, String>> pluginMap) {
        pluginMap = pluginMap;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }
}
