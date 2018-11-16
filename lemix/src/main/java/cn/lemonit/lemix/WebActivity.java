package cn.lemonit.lemix;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tencent.smtt.sdk.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Map;

import cn.lemonit.lemix.base.BaseActivity;
import cn.lemonit.lemix.module.CommunicationInfo;
import cn.lemonit.lemix.web_util.WebviewPool;

public class WebActivity extends BaseActivity {

    private String TAG = "WebActivity";

    private LinearLayout contentLayout;
    private WebView x5WebView;

    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addView(R.layout.activity_web);
        initWebview();
        initData();

    }

    private void initData() {
        bundle = getIntent().getExtras();
        String startPagePath = bundle.getString("__lemix_module_startPagePath");
        x5WebView.loadUrl("file:///" + startPagePath);

    }

    private void initWebview() {
        contentLayout = findViewById(R.id.contentLayout);
        SparseArray<WebView> sparseArray = WebviewPool.getInstance().getSparseArrayWebview();
        int size = sparseArray.size();
        if(size > 0) {
            x5WebView = sparseArray.get(size - 1);
            sparseArray.delete(size - 1);
            if(x5WebView != null) {
                x5WebView.addJavascriptInterface(new AndroidtoJs(), "__js_android__");
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                x5WebView.setLayoutParams(params);
                contentLayout.removeAllViews();
                contentLayout.addView(x5WebView);
            }else {

            }
            // 如果此时池子里面没有了缓存的x5WebView，就先加载一个放池子里面
//            if(sparseArray.size() == 0) {
//                WebView x5WebViewNew = new WebView(getApplicationContext());
//                WebviewPool.getInstance().initX5WebView(sparseArray.size(), x5WebViewNew);
//            }
        }
    }

    /**
     * JS调用Android原生的入口
     */
    public class AndroidtoJs extends Object {
        /**
         * 先获取JS端返回的json数据，然后判断是打开相机还是选取图片等等（包括获取各种参数）
         * 此方法运行在子线程
         *
         * @param msg
         */
        @JavascriptInterface
        public String message(String msg) {

            Object result = 0;
            Log.e(TAG, "msg ====== " + msg);
            Gson gson = new Gson();
            Map<String, Object> map = gson.fromJson(msg, Map.class);
            CommunicationInfo communicationInfo = new CommunicationInfo(WebActivity.this, map);
            String type = communicationInfo.getType();
            String[] arr = type.split("\\.");
//            String className = "com.zhongwang.zwt.core.communication." + arr[0] + "." + arr[1];
            String className = "cn.lemonit.lemix.core.communication." + arr[0] + "." + arr[1];
            try {
                Class clazz = Class.forName(className);
                Method method = null;//clazz.getMethod(arr[2], CommunicationInfo.class);
                for (Method met : clazz.getMethods()) {
                    if (met.getName().equals(arr[2])) {
                        method = met;
                    }
                }
                Object obj = clazz.newInstance();
                // 判断是否有返回值
                result = method.invoke(obj, communicationInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Gson().toJson(result);
        }
    }

    /**
     * 拍照，录像后给JS传递路径
     *
     * @param path
     * @param result
     * 好用： lemage://sandbox/storage/emulated/0/Android/data/com.zhongwang.zwt/camera/photo/a2efd3f2-6263-4e77-9081-212059f5a3e1
     * 现在： lemorage://sandbox/short/e04238c9-aa12-4471-b546-5fca44ec6da8
     */
    public void loadCallback(String path, String result) {
        Log.e(TAG, "拍照 path == " + path);
        Log.e(TAG, "语句 == " + "__load_callback('" + result + "','" + path + "')");
        x5WebView.evaluateJavascript("__load_callback('" + result + "','" + path + "')", null);
    }

    /**
     * 定位信息回调JS
     *
     * @param lon
     * @param lat
     * @param address
     * @param result
     */
    public void getLocation(final String lon, final String lat, final String address, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.e(TAG, "语句 ========= " + "getLocation('" + result + "','" + lon + "','" + lat + "','" + address + "')");
//                x5WebView.evaluateJavascript("getLocation('" + result + "','" + lon + "','" + lat + "','" + address + "','" + "')", null);
//                x5WebView.evaluateJavascript("getLocation('" + lon + "','" + lat + "','" + address + "','" + "')", null);
//                Log.e(TAG, "JS调试 定位回调 x5WebView == " + x5WebView);
                if (x5WebView != null) {
                    x5WebView.evaluateJavascript("__load_callback('" + result + "','" + lon + "','" + lat + "','" + address + "')", null);
//                    Log.e(TAG, "JS调试 定位回调 result == " + result + "  lon == " + lon + "    address == " + address);
                }
            }
        });
    }

    /**
     * 异步请求结果回调给JS
     */
    public void asyncRequestCallBack(final String result, final String body, final String headers, final String code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "语句 == " + "__load_callback('" + result + "','" + body + "','" + headers + "','" + code + "')");
                x5WebView.evaluateJavascript("__load_callback('" + result + "','" + body + "','" + headers + "','" + code + "')", null);
            }
        });

    }

    /**
     * 给外界提供当前的url
     */
    public String getX5WebViewCurrentUrl() {
        return x5WebView.getUrl();
    }


    @Override
    public void onBackPressed() {
        if(x5WebView.canGoBack()) {
            x5WebView.goBack();
        }else {
            Log.e(TAG, "url === " + x5WebView.getUrl());
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (x5WebView != null) {
            x5WebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        x5WebView.onPause();
    }

    @Override
    protected void onDestroy() {
        // 用完x5WebView后，再放回池里
        if(x5WebView != null) {
            contentLayout.removeAllViews();
            SparseArray<WebView> sparseArray = WebviewPool.getInstance().getSparseArrayWebview();
            int size = sparseArray.size();
            sparseArray.put(size, x5WebView);
        }
//        if(x5WebView != null) {
//            x5WebView.destroy();
//            contentLayout.removeAllViews();
//            x5WebView = null;
//            WebView x5WebViewNew = new WebView(getApplicationContext());
//            SparseArray<WebView> sparseArray = WebviewPool.getInstance().getSparseArrayWebview();
//            WebviewPool.getInstance().initX5WebView(sparseArray.size(), x5WebViewNew);
//        }
        super.onDestroy();
        Log.e(TAG, "-- onDestroy --");
    }

    /**
     * 读取assets里面的JS文件
     * @param fileName
     * @param context
     * @return
     */
    private String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
