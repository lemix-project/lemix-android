package cn.lemonit.lemix.lemix;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipFile;

import cn.lemonit.lemix.WebActivity;
import cn.lemonit.lemix.callback.LemixActivityLifecycle;
import cn.lemonit.lemix.callback.LocationOperating;
import cn.lemonit.lemix.callback.MixModuleLifeCycle;
import cn.lemonit.lemix.callback.WaitingPageStandard;
import cn.lemonit.lemix.common.PluginManager;
import cn.lemonit.lemix.mixmodule.MixModuleInfo;
import cn.lemonit.lemix.mixmodule.MixModuleInstance;
import cn.lemonit.lemix.mixmodule.NativePageInfo;
import cn.lemonit.lemix.mixmodule.StartUpMixModuleParameter;
import cn.lemonit.lemix.util.FileUtil;
import cn.lemonit.lemix.util.NetUtil;
import cn.lemonit.lemix.util.ScanFile;
import cn.lemonit.lemix.util.ZipUtil;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 插件引擎类
 * 调用者可以用默认的defaultEngine ，也可以自己创建新的LemixEngine来用
 */
public class LemixEngine {

    private LemixEngineConfig config;
    /**
     * 引擎名称，由调用者传递进来，用于设置该引擎所产生的文件目录，所以，每个引擎有每个单独的目录
     */
    private String engineName;

    private StartUpMixModuleParameter parameter;

    /**
     * 管理插件生命周期和插件信息
     */
    private Map<String, MixModuleInstance> mixModuleInstanceMap = new HashMap<String, MixModuleInstance>();
    private List<MixModuleInstance> instanceList = new ArrayList<MixModuleInstance>();

    /**
     * 分别保存远程，本地，原生的module信息
     */
    private Map<String, MixModuleInfo> mixModulePool = new HashMap<>();
    private Map<String, NativePageInfo> nativePagePool = new HashMap<>();

    /**
     * key : moduleKey + packageKey
     * value : 对应的插件的activity集合
     */
    private Map<String, List<Activity>> pluginListActivity = new HashMap<>();

    // 需要扫描的文件
    private final String scanTargetFileName = "index.html";

    private Application application;

    /**
     * 通过配置对象进行初始化
     * @param config  LemixEngine引擎配置对象
     */
    LemixEngine(String engineName, LemixEngineConfig config, Application application) {
        this.engineName = engineName;
        this.config = config;
        this.application = application;
        application.registerActivityLifecycleCallbacks(lifeCycleCallbacks);
    }

    /**
     * 注册远程MixModule，当MixModule的zip数据包存储在基于HTTP的服务器上需要动态下载时使用
     * 注册远程MixModule后不会立即将MixModule数据包下载到本地，而是在真正使用的时候才动态下载。
     * MixModule数据包被下载完毕时需要将其以moduleKey+packageKey为标识进行缓存，再次需要使用该包时，若缓存中存在，则直接使用缓存中的数据包
     */
    public void registerRemoteMixModule(List<MixModuleInfo> list) {
        // 获取所有的远程插件信息，并将插件名称，图片显示出来(显示插件列表时需要调用),并保存起来
        for(MixModuleInfo moduleInfo : list) {
//            mixModuleMap.put(moduleInfo.getModuleKey() + moduleInfo.getPackageKey(), moduleInfo);
        }
    }

    /**
     * 注册远程插件, 先添加进map集合中, 再去缓存目录（default_temp_path）中找文件，根据packageTime决定是否删除这个缓存文件
     */
    public void registerRemoteMixModule(MixModuleInfo mixModuleInfo) {
        mixModulePool.put(mixModuleInfo.getMixModuleIdentifier() + mixModuleInfo.getPackageTime(), mixModuleInfo);
        // 判断这个插件是否有缓存的ZIP文件
        String filePath = config.getWorkspacePath() + "/" + mixModuleInfo.getModuleName() + "/mixModules";
        String fileName = FileUtil.findFile(filePath, mixModuleInfo);
        // 如果有缓存文件
        if(fileName != null) {
            // 获取缓存文件名的packageTime部分，用来和现在的packageTime比较, 如果不一样删除缓存文件
            String filePackageTime = fileName.substring(fileName.indexOf("-"), fileName.length());
            if(!filePackageTime.equals(mixModuleInfo.getPackageTime())) {
                // 删除文件
                FileUtil.deleteFile(new File(filePath + "/" + fileName));
            }
        }
    }

    /**
     * 注册本地MixModule，本地MixModule不存在缓存机制。
     */
    public void registerLocalMixModule(List<MixModuleInfo> list) {
        // 获取本地的远程插件信息，并将插件名称，图片显示出来(显示插件列表时需要调用),并保存起来
        for(MixModuleInfo moduleInfo : list) {
//            mixModuleMap.put(moduleInfo.getModuleKey() + moduleInfo.getPackageKey(), moduleInfo);
        }
    }

    public void registerLocalMixModule(MixModuleInfo mixModuleInfo) {
//        mixModuleMap.put(mixModuleInfo.getModuleKey() + mixModuleInfo.getPackageKey(), mixModuleInfo);
    }

    /**
     * 注册原生页面，将原生页面注册到LemixEngine时，需要指定这个原生页面的nativePageKey，
     * 之后无论是原生还是JS，均可以通过nativePageKey找到这个原生页面，并打开它。
     */
    public void registerNativePage(List<NativePageInfo> list) {
        // 获取原生界面信息, 并保存起来
        for(NativePageInfo nativePageInfo : list) {
            nativePagePool.put(nativePageInfo.getNativePageKey(), nativePageInfo);
        }
    }


    /**
     * 启动插件
     * @param context
     * @param mixModuleLifeCycle
     * @param parameter
     */
    public void startUpMixModule(Context context, MixModuleLifeCycle mixModuleLifeCycle, final StartUpMixModuleParameter parameter) {
        // 判断此插件是否已经注册
        final MixModuleInfo moduleInfo = mixModulePool.get(parameter.getModuleKey() + parameter.getPackageKey());
        if(moduleInfo == null) {
            return;
        }
        this.parameter = parameter;
//        // 保存MixModuleInstance
        MixModuleInstance mixModuleInstance = new MixModuleInstance();
        mixModuleInstance.setModuleInfo(moduleInfo);
        List<Activity> taskActivityList = new ArrayList<Activity>();
        mixModuleInstance.setTaskActivityList(taskActivityList);
        mixModuleInstance.setModuleLifeCycle(mixModuleLifeCycle);
        instanceList.add(mixModuleInstance);
//        mixModuleInstanceMap.put(parameter.getModuleKey() + parameter.getPackageKey(), mixModuleInstance);
        // 启动新栈启动等待页
        Intent intent = new Intent(context, config.getWaitPager());
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        // 记录是哪个插件
        Bundle bundle = new Bundle();
        bundle.putString("__lemix_engine_name", engineName);
        bundle.putString("__lemix_module_identifier", parameter.getModuleKey());
        intent.putExtras(bundle);
        context.startActivity(intent);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 判断是本地插件还是远程插件
                File fileZip = null;
                if(moduleInfo.getMixModuleURL().startsWith("http")) {
                    // 如果是远程插件，判断是否有缓存
                    String filePath = config.getWorkspacePath() + "/" + moduleInfo.getModuleName() + "/mixModules";
                    String fileName = FileUtil.findFile(filePath, moduleInfo);
                    // 没有缓存
                    if(TextUtils.isEmpty(fileName)) {
                        // 下载ZIP文件后拿ZIP文件
                        String loadFileName = moduleInfo.getMixModuleIdentifier() + "-" + moduleInfo.getPackageTime() + ".zip";
                        String savePath = config.getWorkspacePath() + "/" + engineName + "/mixModules/";
                        // 创建对应的文件夹
                        createFiles();
                        NetUtil.downLoad(moduleInfo, loadFileName, savePath);
                    }else {
                        // 直接拿ZIP文件
                    }
                }else {
                    // 直接拿ZIP文件
                }
                // 删除temp解压的原来的文件
                String unZipPath = config.getTempPath() + "/" + engineName + "/mixModules/" + moduleInfo.getMixModuleIdentifier() + "-" + moduleInfo.getPackageTime();
                File file = new File(unZipPath);
                FileUtil.deleteFile(file);
                File file1 = new File(unZipPath);
                if(!file1.exists()) {
                    file1.mkdirs();
                }
                // 解压
                String zipPath = config.getWorkspacePath() + "/" + engineName + "/mixModules/" + moduleInfo.getMixModuleIdentifier() + "-" + moduleInfo.getPackageTime() + ".zip";
                boolean zip = false;
                try {
                    zip = ZipUtil.unZip(zipPath, unZipPath);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Activity", "解压错误");
                }
                Log.e("Activity", "解压结果 ==== " + zip);

                // 扫描
                Map<String, Map<String, String>> pluginMap = PluginManager.getPluginManager().getPluginMap();
                Map<String, String> map = new HashMap<>();
                ScanFile.scanFile(new File(unZipPath), scanTargetFileName, parameter.getModuleKey(), map);
                pluginMap.put(parameter.getModuleKey(), map);
                PluginManager.getPluginManager().setPluginName(parameter.getModuleKey());
                // 查看启动参数
                String startPagePath = null;
                if(TextUtils.isEmpty(parameter.getJson())) {
                    // 没有设置启动参数，那么就打开entrance中指定的MixPage
                    Set set = map.keySet();
                    for(Iterator iter = set.iterator(); iter.hasNext();) {
                        String key = (String)iter.next();
                        if(key.contains(parameter.getStartPager())) {
                            startPagePath = map.get(key);
                            break;
                        }
                    }
                }else {
                    // 跳转到设置的界面
                }

                // 跳转
                Message msg = Message.obtain();
                msg.what = 100;
                msg.obj = startPagePath;
                handler.sendMessage(msg);


                // 删除temp解压的文件
                // 扫描MixModule下面有哪些MixPage
                // 查看启动参数中是否有startUpMixPage信息，如果设置了信息，直接启动，否则，读取MixModule配置文件，设置启动页面为entrance中指定的MixPage
                // 启动MixPage
                // 把activity添加到对应的MixModuleInstance中
            }
        }).start();

//        loadPlugin(pluginKey);

//        // 下载，解压，扫描
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // 下载
//                    Thread.sleep(2000);
//                    Message messageLoad = Message.obtain();
//                    messageLoad.what = 100;
//                    messageLoad.obj = pluginKey;
//                    handler.sendMessage(messageLoad);
//                    // 解压
//                    Thread.sleep(1000);
//                    Message messageZip = Message.obtain();
//                    messageZip.what = 200;
//                    messageZip.obj = pluginKey;
//                    handler.sendMessage(messageZip);
//                    // 扫描
//                    Thread.sleep(1000);
//                    Message messageScan = Message.obtain();
//                    messageScan.what = 300;
//                    messageScan.obj = pluginKey;
//                    handler.sendMessage(messageScan);
//                    // 启动插件页
//                    Message messagePlugin = Message.obtain();
//                    messagePlugin.what = 400;
//                    messagePlugin.obj = pluginKey;
//                    handler.sendMessage(messagePlugin);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 跳转到具体插件页
                case 100:
                    // 循环遍历拿到等待页实例
                    Activity waitActivity = null;
                    MixModuleInstance mixModuleInstance = instanceList.get(instanceList.size() - 1);
                    List<Activity> list = mixModuleInstance.getTaskActivityList();
                    waitActivity = list.get(0);
                    Intent intent = new Intent(application.getApplicationContext(), WebActivity.class);
                    // 重写启动一个栈
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    // 记录是哪个插件
                    Bundle bundle = new Bundle();
                    bundle.putString("__lemix_engine_name", engineName);
                    bundle.putString("__lemix_module_identifier", parameter.getModuleKey());
                    String path = (String) msg.obj;
                    bundle.putString("__lemix_module_startPagePath", path);
                    intent.putExtras(bundle);
                    application.getApplicationContext().startActivity(intent);
                    // 关闭对应的等待界面
                    if(waitActivity != null) {
                        waitActivity.finish();
                        list.remove(waitActivity);
                    }
                    break;
            }
        }
    };


//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            // 循环遍历拿到等待页实例
//            Activity waitActivity = null;
//            String pluginKey = parameter.getModuleKey() + parameter.getPackageKey();
//            List<Activity> list = pluginListActivity.get(pluginKey);
//            for(Activity activity : list) {
//                if(activity instanceof WaitingPageStandard) {
//                    waitActivity = activity;
//                    break;
//                }
//            }
//            switch (msg.what) {
//                // 下载结束通知等待页
//                case 100:
//                    if(waitActivity != null) {
//                        ((WaitingPageStandard) waitActivity).onLoad();
//                    }
//                    break;
//                // 解压结束通知等待页
//                case 200:
//                    if(waitActivity != null) {
//                        ((WaitingPageStandard) waitActivity).onUnZip();
//                    }
//                    break;
//                // 扫描结束通知等待页
//                case 300:
//                    if(waitActivity != null) {
//                        ((WaitingPageStandard) waitActivity).onScan();
//                    }
//                    break;
//                // 扫描结束启动插件页
//                case 400:
//                    String key = (String) msg.obj;
//                    showPlugin(key);
//                    Intent intent = new Intent( Lemix.application.getApplicationContext(), WebActivity.class);
//                    // 重写启动一个栈
//                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                    // 记录是哪个插件
//                    Bundle bundle = new Bundle();
//                    bundle.putString("pluginKey", pluginKey);
//                    intent.putExtras(bundle);
//                    Lemix.application.getApplicationContext().startActivity(intent);
//                    // 关闭对应的等待界面
//                    if(waitActivity != null) {
//                        waitActivity.finish();
//                        list.remove(waitActivity);
//                    }
//                    break;
//            }
//        }
//    };

    private void createFiles() {
        File fileWorkspace = new File(config.getWorkspacePath());
        if(!fileWorkspace.exists()) {
            fileWorkspace.mkdirs();
        }
        File fileWorkspaceEngineName = new File(fileWorkspace, engineName);
        if(!fileWorkspaceEngineName.exists()) {
            fileWorkspaceEngineName.mkdirs();
        }
        File fileWorkspaceModule = new File(fileWorkspaceEngineName, "mixModules");
        if(!fileWorkspaceModule.exists()) {
            fileWorkspaceModule.mkdirs();
        }
        File fileTemp = new File(config.getTempPath());
        if(!fileTemp.exists()) {
            fileTemp.mkdirs();
        }
        File fileTempEngineName = new File(fileTemp, engineName);
        if(!fileTempEngineName.exists()) {
            fileTempEngineName.mkdirs();
        }
        File fileTempModule = new File(fileTempEngineName, "mixModules");
        if(!fileTempModule.exists()) {
            fileTempModule.mkdirs();
        }
    }

    /**
     * 生命周期
     */
    private LemixActivityLifecycle lifeCycleCallbacks = new LemixActivityLifecycle() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            super.onActivityCreated(activity, savedInstanceState);
            // 每个启动的activity都要判断，如果是插件类的activity，加入对应的集合中
            Bundle bundle = activity.getIntent().getExtras();
            if(bundle == null) {
                return;
            }
            String __lemix_engine_name = bundle.getString("__lemix_engine_name");
            String __lemix_module_identifier = bundle.getString("__lemix_module_identifier");
            // 是插件activity
            if(!TextUtils.isEmpty(__lemix_engine_name) && !TextUtils.isEmpty(__lemix_module_identifier)) {
                // 取最后一个栈, 添加当前activity
                MixModuleInstance mixModuleInstance = instanceList.get(instanceList.size() - 1);
                List<Activity> taskActivityList = mixModuleInstance.getTaskActivityList();
                taskActivityList.add(activity);

//                List<Activity> list = pluginListActivity.get(pluginKey);
//                if(list == null) {
//                    list = new ArrayList<Activity>();
//                    pluginListActivity.put(pluginKey, list);
//                }
//                list.add(activity);
//
//                // 如果是等待页，把插件信息传递给等待页
//                if(activity instanceof WaitingPageStandard) {
//                    MixModuleInfo mixModuleInfo = mixModulePool.get(pluginKey);
//                    if(mixModuleInfo != null) {
//                        ((WaitingPageStandard) activity).onWaiting(mixModuleInfo);
//                    }
//                }
            }

        }

        @Override
        public void onActivityStarted(Activity activity) {
            super.onActivityStarted(activity);
            // 把当前打开的栈放最后一个位置
            for(MixModuleInstance mixModuleInstance : instanceList) {
                List<Activity> list = mixModuleInstance.getTaskActivityList();
                for(Activity activityInstance : list) {
                    if(activityInstance == activity) {
                        instanceList.remove(mixModuleInstance);
                        instanceList.add(mixModuleInstance);
                        return;
                    }
                }
            }
        }
    };


    /**
     * 关闭某个插件的全部界面
     */
    public void closePluginAll(String pluginKey) {
        List<Activity> list = pluginListActivity.get(pluginKey);
        if(list == null) {
            return;
        }
        for(Activity activity : list) {
            activity.finish();
        }
        // 回调给调用者（打开插件的界面）
        mixModuleInstanceMap.get(pluginKey).getModuleLifeCycle().onClose(pluginKey);
    }
    /**
     * 隐藏某个插件的全部界面
     */
    public void hidePluginAll(String pluginKey) {
        List<Activity> list = pluginListActivity.get(pluginKey);
        if(list == null) {
            return;
        }
        for(Activity activity : list) {
            activity.moveTaskToBack(true);
        }
        // 回调给调用者（打开插件的界面）
        mixModuleInstanceMap.get(pluginKey).getModuleLifeCycle().onHide(pluginKey);
    }

    /**
     * 插件开始下载的时候
     * @param pluginKey
     */
    private void loadPlugin(String pluginKey) {
        mixModuleInstanceMap.get(pluginKey).getModuleLifeCycle().onLoad(pluginKey);
    }

    /**
     * 插件开始显示的时候
     * @param pluginKey
     */
    private void showPlugin(String pluginKey) {
        mixModuleInstanceMap.get(pluginKey).getModuleLifeCycle().onShow(pluginKey);
    }

    /**
     * 获取所有注册的远程和本地mixModule信息
     * @return
     */
    public Map<String, MixModuleInfo> getMixModuleMap() {
        return mixModulePool;
    }


    public LocationOperating getLocationOperating() {
        return config.getLocationOperating();
    }
}
