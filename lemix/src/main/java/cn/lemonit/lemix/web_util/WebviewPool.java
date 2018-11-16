package cn.lemonit.lemix.web_util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.lemorage.file.Lemorage;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * webview池，程序启动时就初始化5个，随用随放，当5个都用掉就再增加
 */
public class WebviewPool {

    private Context context;

    private WebviewPool(){}

    private static WebviewPool instance;

    /**
     * WebView保存在SparseArray中，先初始化5个
     */
    private SparseArray<WebView> sparseArrayWebview = new SparseArray<WebView>();
    public static WebviewPool getInstance() {
        if(instance == null) {
            instance = new WebviewPool();
        }
        return instance;
    }

    /**
     * 初始化WebView
     */
    public void initWebView(Context mContext) {
        for(int i = 0; i < 5; i ++) {
            WebView x5WebView = new WebView(mContext);
            context = mContext;
            initX5WebView(i, x5WebView);
        }
    }

    //

    public void initX5WebView(int index, WebView x5WebView) {
        if(x5WebView != null) {
            WebSettings settings = x5WebView.getSettings();
            x5WebView.setWebViewClient(x5WebViewClient);
            x5WebView.setWebChromeClient(x5WebChromeClient);
            settings.setJavaScriptEnabled(true);
            settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
            //设置加载图片
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setDefaultTextEncodingName("utf-8");// 避免中文乱码
            x5WebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            settings.setNeedInitialFocus(false);
            settings.setSupportZoom(true);
            settings.setLoadWithOverviewMode(true);//适应屏幕
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setLoadsImagesAutomatically(true);//自动加载图片
//        settings.setCacheMode(WebSettings.LOAD_DEFAULT | WebSettings.LOAD_CACHE_ELSE_NETWORK);
            // 设置不使用缓存
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            settings.setAppCacheEnabled(false);
            IX5WebViewExtension mIX5WebViewExtension = x5WebView.getX5WebViewExtension();
            // 代码设置隐藏滚动条， XML中设置为 android:scrollbars="none"
            if(mIX5WebViewExtension != null) {
                mIX5WebViewExtension.setScrollBarFadingEnabled(false);
            }
//        x5WebView.addJavascriptInterface(new AndroidtoJs(), "__js_android__");
            sparseArrayWebview.put(index, x5WebView);
        }
    }

    public class AndroidtoJs extends Object {
        @JavascriptInterface
        public String message(String msg) {
            Log.e("ZWTWebActivity", "AndroidtoJs   msg == " + msg);
            return "";
        }
    }

    private WebViewClient x5WebViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            String js = getJson("lemix.min.js", context);
//            Log.e("ZWTWebActivity", "JS == " +js);
            webView.evaluateJavascript(js, null);
            webView.evaluateJavascript("$__set_platform__('android')", null);
            webView.evaluateJavascript("__onload()", null);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            webView.loadUrl(url);
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
            if (url.startsWith("lemage://sandbox") || url.startsWith("lemorage://sandbox")) {
                WebResourceResponse response = null;
                try {
                    File file = Lemorage.getWithFile(url, context);
                    if (file != null) {
                        InputStream localCopy = new FileInputStream(file);
                        response = new WebResourceResponse("image/png", "UTF-8", localCopy);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return response;
            } else {
                return super.shouldInterceptRequest(webView, url);
            }
//            return super.shouldInterceptRequest(webView, url);
        }

        @Override
        public void doUpdateVisitedHistory(WebView webView, String url, boolean isReload) {
            super.doUpdateVisitedHistory(webView, url, isReload);
            webView.clearHistory();//清除历史记录
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
//            super.onReceivedSslError(webView, sslErrorHandler, sslError);
            sslErrorHandler.proceed();  //接受所有证书
        }
    };

    private WebChromeClient x5WebChromeClient = new WebChromeClient() {
        /**
         * 进度条
         * @param webView
         * @param newProgress
         */
        @Override
        public void onProgressChanged(WebView webView, int newProgress) {
            super.onProgressChanged(webView, newProgress);
        }

        public boolean onJsAlert(WebView view, String url, final String message, final JsResult result) {
//            new AlertDialog.Builder().setTitle("Alert对话框").setMessage(message + "AlertMessage")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            result.confirm();
//                        }
//                    }).setCancelable(false).show();
            return true;
        }
    };

    public SparseArray<WebView> getSparseArrayWebview() {
        return sparseArrayWebview;
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
