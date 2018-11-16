package cn.lemonit.lemix.core.operate.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import cn.lemonit.lemix.R;
import cn.lemonit.lemix.WebActivity;
import cn.lemonit.lemix.base.BaseActivity;
import cn.lemonit.lemix.callback.MixModuleLifeCycle;
import cn.lemonit.lemix.common.PluginManager;
import cn.lemonit.lemix.define.ActivityMappingDefine;
import cn.lemonit.lemix.module.AimActivityInfo;

/**
 * 界面切换管理的真正执行类
 */
public class UiNavigation {

    /**
     * 相同维度的activity往下跳转
     * @param fromActivity
     * @param aimActivityInfo
     */
    public void push(final BaseActivity fromActivity, AimActivityInfo aimActivityInfo, Map<String, Object> map) {
        // 如果aimActivityInfo是null，是原生往原生跳，和HTML一点关系没有，此时，toActivity在map中取, 携带数据也在map中取
        Class toClass = null;
        final Intent intent = new Intent();
        // aimActivityInfo == null肯定是原生界面来的（即不是ZWTWebActivity）
        if(aimActivityInfo == null) {
            toClass = (Class) map.get("toActivity");
            if(toClass == WebActivity.class) {
                intent.putExtra("path", (String)(map.get("path")));
            }
        }
        // 此时，是html往html跳，或者html和原生混合跳转
        else {
            String path = "";
            toClass = WebActivity.class;
            WebActivity zwtFromActivity = (WebActivity) fromActivity;
            // 往原生跳
            if(aimActivityInfo.getType().equals("native")) {
                toClass = ActivityMappingDefine.getMapping(fromActivity).get(aimActivityInfo.getAim());
            }
            if(map != null) {
                String aim = (String) map.get("aim");
                String type = (String) map.get("type");
//                String oldPath = zwtFromActivity.getX5WebView().getUrl();
                String oldPath = zwtFromActivity.getX5WebViewCurrentUrl();
                if(!TextUtils.isEmpty(type) && !TextUtils.isEmpty(aim)) {
                    if(type.equals("relative")) {
                        path = getPath(oldPath, aim);
                    }else if(type.equals("absolute")) {
                        path = aim;
                    }else if(type.equals("ext")) {
                        Map<String, Map<String, String>> pluginMap = PluginManager.getPluginManager().getPluginMap();
                        String mapKey = PluginManager.getPluginManager().getPluginName();
                        // 获取了具体插件的map
                        Map<String, String> mapPluginIndex = pluginMap.get(mapKey);
//                        String indexKey = mapKey + "." + aim;
                        String indexKey = mapKey + "/" + aim;
                        Log.e("ZWTW", "ext  indexKey === " + indexKey);
                        path = mapPluginIndex.get(indexKey);
                        Log.e("ZWTW", "ext  path === " + path);
                        // 获取config
                        String configStr = readToString(path.replace("index.html", "config.json"));
                        Gson gson = new Gson();
                        Map<String, Object> configMap = gson.fromJson(configStr, Map.class);
                        map.put("config", configMap);
                    }
                    intent.putExtra("__lemix_module_startPagePath", path);
                }
            }
        }
        // 获取状态栏，标题栏显示信息
        Map<String, Object> configMap = (Map<String, Object>) map.get("config");
        if(configMap != null) {
            // 标题栏是否隐藏
            boolean navigationHidden = configMap.get("navigationHidden") == null ? false : (boolean) configMap.get("navigationHidden");
            intent.putExtra("navigationHidden", navigationHidden);
            // 标题栏背景颜色
            String navigationBackgroundColor = (String) configMap.get("navigationBackgroundColor");
            intent.putExtra("navigationBackgroundColor", navigationBackgroundColor);
            // 标题栏字体颜色
            String navigationItemColor = (String) configMap.get("navigationItemColor");
            intent.putExtra("navigationItemColor", navigationItemColor);
            // 标题栏标题
            String navigationTitle = (String) configMap.get("navigationTitle");
            intent.putExtra("navigationTitle", navigationTitle);
            // 状态栏是否隐藏
            boolean statusBarHidden = configMap.get("statusBarHidden") == null ? false : (boolean) configMap.get("statusBarHidden");
            intent.putExtra("statusBarHidden", statusBarHidden);
            // 状态栏字体颜色（6.0 系统）
            String statusBarStyle = (String) configMap.get("statusBarStyle");
            intent.putExtra("statusBarStyle", statusBarStyle);
        }

        final Class finalToClass = toClass;

        fromActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                intent.setClass(fromActivity, finalToClass);
                intent.putExtra("action", "push");
                // 这两行不写在主线程时，界面切换动画就不稳定，有时候显示，有时候不显示
                fromActivity.startActivity(intent);
//                fromActivity.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_out_to_left);
                if(fromActivity instanceof MixModuleLifeCycle) {
                    fromActivity.overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
                }else {
                    fromActivity.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_out_to_left);
                }
            }
        });

    }

//    /**
//     * 不同维度的activity往下跳转（一维往二维跳转）
//     * @param fromActivity
//     * @param aimActivityInfo
//     */
//    public void present(final BaseActivity fromActivity, AimActivityInfo aimActivityInfo, Map<String, Object> map) {
//        // 如果aimActivityInfo是null，是原生往原生跳，和HTML一点关系没有，此时，toActivity在map中取, 携带数据也在map中取
//        Class toClass = null;
//        final Intent intent = new Intent();
//        if(aimActivityInfo == null) {
//            toClass = (Class) map.get("toActivity");
//            if(toClass == WaitingActivity.class) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("EXTBeen", (Serializable) map.get("EXTBeen"));
//                intent.putExtras(bundle);
//            }
//        }
//        // 此时，是html往html跳，或者html和原生混合跳转
//        else {
//            String path = "";
//            toClass = WebActivity.class;
//            WebActivity zwtFromActivity = (WebActivity) fromActivity;
//            // 往原生跳
//            if(aimActivityInfo.getType().equals("native")) {
//                toClass = ActivityMappingDefine.getMapping(fromActivity).get(aimActivityInfo.getAim());
//            }
//            if(map != null) {
//                String aim = (String) map.get("aim");
//                String type = (String) map.get("type");
////                String oldPath = zwtFromActivity.getX5WebView().getUrl();
//                String oldPath = zwtFromActivity.getX5WebViewCurrentUrl();
//                if(!TextUtils.isEmpty(type) && !TextUtils.isEmpty(aim)) {
//                    if(type.equals("relative")) {
//                        path = getPath(oldPath, aim);
//                    }else if(type.equals("absolute")) {
//                        path = aim;
//                    }
//                    intent.putExtra("path", path);
//                }
//            }
//        }
//        // 获取状态栏，标题栏显示信息
//        Map<String, Object> configMap = (Map<String, Object>) map.get("config");
//        if(configMap != null) {
//            // 标题栏是否隐藏
//            boolean navigationHidden = (boolean) configMap.get("navigationHidden");
//            intent.putExtra("navigationHidden", navigationHidden);
//            // 标题栏背景颜色
//            String navigationBackgroundColor = (String) configMap.get("navigationBackgroundColor");
//            intent.putExtra("navigationBackgroundColor", navigationBackgroundColor);
//            // 标题栏字体颜色
//            String navigationItemColor = (String) configMap.get("navigationItemColor");
//            intent.putExtra("navigationItemColor", navigationItemColor);
//            // 标题栏标题
//            String navigationTitle = (String) configMap.get("navigationTitle");
//            intent.putExtra("navigationTitle", navigationTitle);
//            // 状态栏是否隐藏
//            boolean statusBarHidden = (boolean) configMap.get("statusBarHidden");
//            intent.putExtra("statusBarHidden", statusBarHidden);
//            // 状态栏字体颜色（6.0 系统）
//            String statusBarStyle = (String) configMap.get("statusBarStyle");
//            intent.putExtra("statusBarStyle", statusBarStyle);
//        }
//
//        final Class finalToClass = toClass;
//        fromActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                // 这两行不写在主线程时，界面切换动画就不稳定，有时候显示，有时候不显示
//                intent.setClass(fromActivity, finalToClass);
//                intent.putExtra("action", "present");
//                fromActivity.startActivity(intent);
//                fromActivity.overridePendingTransition(R.anim.activity_in_from_bottom, R.anim.activity_out_to_top);
//            }
//        });
//
//    }
//
//    /**
//     * 相同维度的activity往回跳转
//     * 可能会同一级别的activity往回跳转好几个
//     * @param fromActivity   回跳到目标activity
//     * @param count    期间经过几个activity
//     */
//    public void pop(BaseActivity fromActivity, int count) {
////        ActivityStackManager.getInstance().pop(fromActivity, count);
//    }
//
//    /**
//     * 不同维度的activity往回跳转（二维往一维跳转）
//     */
//    public void close(BaseActivity fromActivity, int count) {
////        ActivityStackManager.getInstance().close(fromActivity, count);
//    }
//
//    /**
//     * activity在finish时不需要效果，此时是默默的关闭掉，这时只是刷新栈信息
//     * @param fromActivity
//     */
//    public void finishActivity(BaseActivity fromActivity) {
////        ActivityStackManager.getInstance().finishActivity(fromActivity);
//    }
//
//
    /**
     * 根据相对路径获取绝对路径
     * @param pathFrom
     * @param pathTo
     * @return
     */
    public static String getPath(String pathFrom, String pathTo) {
        String path = null;
        // 去掉最后一位“/”
        if(pathFrom.endsWith("/")) {
            pathFrom = pathFrom.substring(0, pathFrom.length() - 1);
        }
        if(!pathTo.startsWith("/")) {
            pathTo = "/" + pathTo;
        }
        String[] fromArr = pathFrom.split("/");
        String[] toArr = pathTo.split("/");
        StringBuffer sbFrom = new StringBuffer();
        StringBuffer sbTo = new StringBuffer();
        int pointB = 0;   // pathTo里面的点点数量
        for(int i = 0; i < toArr.length; i ++) {
            if(toArr[i].equals("..")) {
                pointB ++;
                continue;
            }
            // 如果一个点就直接过滤
            if(toArr[i].equals(".") || toArr[i].equals("")) {
                continue;
            }
            sbTo.append("/");
            sbTo.append(toArr[i]);
        }
        sbTo.substring(0, sbTo.length() - 2);  // 去掉最后一个“/”
        System.out.println("sbTo == " + sbTo);
        System.out.println("fromArr.length == " + fromArr.length);
        if(fromArr.length < 4) {
            path = pathFrom + pathTo;
        }else {
            for(int i = 0; i < fromArr.length - 1 - pointB; i ++) {
                if(fromArr[i].equals("")) {
                    sbFrom.append("/");
                    continue;
                }
                sbFrom.append(fromArr[i]);
                if(i < fromArr.length - pointB - 2) {
                    sbFrom.append("/");
                }
            }
            System.out.println("sbFrom == " + sbFrom);
            path = sbFrom.toString() + sbTo.toString();
        }
        return path;
    }


    private String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
