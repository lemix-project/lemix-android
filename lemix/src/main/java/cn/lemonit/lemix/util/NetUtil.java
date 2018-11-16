package cn.lemonit.lemix.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.lemonit.lemix.WebActivity;
import cn.lemonit.lemix.mixmodule.MixModuleInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 网络请求工具类
 */
public class NetUtil {

    private String TAG = "Activity NetUtil";

    public static String result = "12123123123";

    /**
     * 同步GET
     */
    public Map syncRequestGet(final WebActivity activity, final Map<String, Object> map) {
        // 设置超时请求
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        String url = (String) map.get("url");
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        final Map<String, Object> mapBack = new HashMap<String, Object>();

        try {
            Response response = call.execute();
            String code = String.valueOf(response.code());
            if (response.isSuccessful()){
                String body = response.body().string();
                String headers = response.headers().toString();
                Log.e(TAG, "body == " + body);
                Log.e(TAG, "headers == " + headers);
                Log.e(TAG, "code == " + code);
                // 1 UUID 2 body 3 headers 4 code
//                activity.syncRequestGetCallBack((String) map.get("success"), body, headers, code);
//                activity.syncRequestGetCallBack((String) map.get("success"), body, headers, code);
                Thread.sleep(1000);
                mapBack.put("success", true);
                mapBack.put("body", body);
                mapBack.put("headers", headers);
                mapBack.put("code", code);
//                return new Gson().toJson(mapBack);
            }else {
                Log.e(TAG, "请求失败");
                mapBack.put("success", false);
                mapBack.put("code", code);
                mapBack.put("body", "请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "请求失败 error == " + e.toString());
            mapBack.put("success", false);
            mapBack.put("error", e.toString());
        }
        return mapBack;
    }

    /**
     * 同步POST
     */
    public Map syncRequestPost(WebActivity activity, Map<String, Object> map) {
        // 设置超时请求
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        // 获取请求参数，此处是JSON
        String params = (String) map.get("body");
        String url = (String) map.get("url");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        // 返回的map
        final Map<String, Object> mapBack = new HashMap<String, Object>();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String code = String.valueOf(response.code());
            if(response.isSuccessful()) {
                String bodyS = response.body().string();
                String headers = response.headers().toString();
                Log.e(TAG, "body == " + bodyS);
                Log.e(TAG, "headers == " + headers);
                Log.e(TAG, "code == " + code);
                mapBack.put("success", true);
                mapBack.put("body", body);
                mapBack.put("headers", headers);
                mapBack.put("code", code);
            }else {
                Log.e(TAG, "code == " + code);
                Log.e(TAG, "请求失败");
                mapBack.put("success", false);
                mapBack.put("code", code);
                mapBack.put("body", "请求失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            mapBack.put("success", false);
            mapBack.put("error", e.toString());
        }
        return mapBack;
    }

    /**
     * 异步get请求
     * @param url
     */
    public void asyncRequestGet(final Context context, String url, final RequestCallBack callBack) {
        final String result = null;
        // 设置超时请求
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .build();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Toast.makeText(context, "请求失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "asyncRequestGet 请求失败：" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if(code == 200) {
                    String body = response.body().string();
                    callBack.callBack(body);
                }
            }
        });
    }

    /**
     * 异步get请求
     */
    public void asyncRequestGet(final WebActivity activity, final Map<String, Object> map) {
        // 设置超时请求
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        String url = (String) map.get("url");
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.asyncRequestCallBack((String) map.get("failed"), e.getMessage(), "", "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.e(TAG, "body == " + body);
                Headers headers = response.headers();
                // 此时的headers不是JSON，把headers转换成map,再转换成JSON返回
                int responseHeadersLength = headers.size();
                HashMap<String, String> headerMap = new HashMap<String, String>();
                for (int i = 0; i < responseHeadersLength; i++) {
                    String headerName = headers.name(i);
                    String headerValue = headers.get(headerName);
                    headerMap.put(headerName, headerValue);
                }
                Gson gson = new Gson();
                // 得到header的JSON形式
                String headersJson = gson.toJson(headerMap);
                Log.e(TAG, "headersJson == " + headersJson);
                String code = String.valueOf(response.code());
                if(code.equals("200")) {
                    activity.asyncRequestCallBack((String) map.get("success"), body, headersJson, code);
                }
                activity.asyncRequestCallBack((String) map.get("failed"), body, headersJson, code);
            }
        });
    }

    /**
     * 异步post请求
     */
    public void asyncRequestPost(final WebActivity activity, final Map<String, Object> map) {
        // 设置超时请求
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        // 获取请求参数，此处是JSON
        String params = (String) map.get("body");
        String url = (String) map.get("url");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.asyncRequestCallBack((String) map.get("failed"), e.getMessage(), "", "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.e(TAG, "body == " + body);
                Headers headers = response.headers();
                // 此时的headers不是JSON，把headers转换成map,再转换成JSON返回
                int responseHeadersLength = headers.size();
                HashMap<String, String> headerMap = new HashMap<String, String>();
                for (int i = 0; i < responseHeadersLength; i++) {
                    String headerName = headers.name(i);
                    String headerValue = headers.get(headerName);
                    headerMap.put(headerName, headerValue);
                }
                Gson gson = new Gson();
                // 得到header的JSON形式
                String headersJson = gson.toJson(headerMap);
                Log.e(TAG, "headersJson == " + headersJson);
                String code = String.valueOf(response.code());
                if(code.equals("200")) {
                    activity.asyncRequestCallBack((String) map.get("success"), body, headersJson, code);
                }
                activity.asyncRequestCallBack((String) map.get("failed"), body, headersJson, code);
            }
        });
    }


    /**
     * 上传
     * @param activity
     * @param map
     */
    public void uploadFile(final WebActivity activity, final Map<String, Object> map) {
        // {"type":"net.http.uploadFile","params":{"url":"http://192.168.11.129:3385/file/upload","method":"POST","header":{"Accept":"*/*"},"fileKey":"lemage://sandbox/storage/emulated/0/Android/data/com.zhongwang.zwt/camera/photo/80a82288-837f-46b3-93ff-cf4d81a09bff","success":"59AF6333-AB30-4287-9DEE-F6155338EAB3","failed":"94EB8CCA-CA22-4132-B64D-0931B8717FED"}}
        // 设置超时请求
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        MultipartBody.Builder mMultipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        String path = (String) map.get("fileKey");
        if(path.startsWith("lemage://sandbox")) {
            path = path.substring(16);
        }
        Log.e(TAG, "path ==== " + path);
        File file = new File(path);
        if(file == null) return;
        // 第一个参数是上传文件的key, 第二个参数是文件路径, 第三参数是RequestBody
        Log.e(TAG, "参数1 == " + "lemage://sandbox" + path);
        Log.e(TAG, "参数2 == " + file.getName());
//        mMultipartBody.addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        mMultipartBody.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
//        MediaType type = MediaType.parse("application/octet-stream");//"text/xml;charset=utf-8"
//        RequestBody mRequestBody = RequestBody.create(type,file);
        RequestBody mRequestBody = mMultipartBody.build();
        Request request = new Request.Builder()
                .url((String) map.get("url"))
                .post(mRequestBody)

                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.asyncRequestCallBack((String) map.get("failed"), e.getMessage(), "", "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.e(TAG, "上传 body == " + body);
                Headers headers = response.headers();
                // 此时的headers不是JSON，把headers转换成map,再转换成JSON返回
                int responseHeadersLength = headers.size();
                HashMap<String, String> headerMap = new HashMap<String, String>();
                for (int i = 0; i < responseHeadersLength; i++) {
                    String headerName = headers.name(i);
                    String headerValue = headers.get(headerName);
                    headerMap.put(headerName, headerValue);
                }
                Gson gson = new Gson();
                // 得到header的JSON形式
                String headersJson = gson.toJson(headerMap);
                Log.e(TAG, "上传 headersJson == " + headersJson);
                String code = String.valueOf(response.code());
                if(code.equals("200")) {
                    activity.asyncRequestCallBack((String) map.get("success"), body, headersJson, code);
                }else {
                    activity.asyncRequestCallBack((String) map.get("failed"), body, headersJson, code);
                }
            }
        });
    }

    /**
     * 下载
     * @param downloadUrl
     * @param outputUrl      下载到哪个文件夹中
     * @param targetFileName   下载后的文件的名称
     * @param callBack
     */
    public void downloadFile(String downloadUrl, String outputUrl, String targetFileName, final RequestCallBack callBack) {
        final File targetParentFile = new File(outputUrl);
        if (!targetParentFile.exists()) {
            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
            targetParentFile.mkdirs();
        }
        // 目标文件，后缀是.zip的文件
        final File targetFile = new File(targetParentFile, targetFileName);
        // 设置超时请求
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(downloadUrl).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200) {
                    ResponseBody body = response.body();
                    // 获取文件总长度
                    final long totalLength = body.contentLength();
                    //以流的方式进行读取
                    try {
                        InputStream inputStream = body.byteStream();
                        FileOutputStream outputStream = new FileOutputStream(targetFile);
                        byte[] buffer = new byte[2048];
                        int len = 0;
                        int num = 0;
                        while ((len = inputStream.read(buffer)) != -1){
                            num+=len;
                            outputStream.write(buffer,0,len);
                            final int finalNum = num;
                            Log.e(TAG, "下载进度 == " + finalNum *100/totalLength);
                        }
                        //读取完关闭流
                        outputStream.flush();
                        if(inputStream != null) {
                            inputStream.close();
                        }
                        if(outputStream != null) {
                            outputStream.close();
                        }
                        callBack.callBack("OK");
                    }catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "error == " + e.toString());
                    }
                }
            }
        });
    }

    /**
     * 下载
     * @param activity
     * @param map
     */
    public void downloadFile(final WebActivity activity, final Map<String, Object> map) {
//        // 下载文件保存目录文件夹
//        String tagetParentPath = FileUtil.getInstance(activity).getDownLoadFilePath();
//        final File targetParentFile = new File(tagetParentPath);
//        if (!targetParentFile.exists()) {
//            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
//            targetParentFile.mkdirs();
//        }
//        // 具体的文件名称
//        String url = (String) map.get("url");
//        String fileName = url.substring(url.lastIndexOf("/") + 1);
//        // 具体的文件
//        final File targetFile = new File(tagetParentPath + "/" + fileName);
//
//        String downloadUrl = (String) map.get("url");
//        // 设置超时请求
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .build();
//        Request request = new Request.Builder().url(downloadUrl).get().build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                activity.asyncRequestCallBack((String) map.get("failed"), e.getMessage(), "", "");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Headers headers = response.headers();
//                // 此时的headers不是JSON，把headers转换成map,再转换成JSON返回
//                int responseHeadersLength = headers.size();
//                HashMap<String, String> headerMap = new HashMap<String, String>();
//                for (int i = 0; i < responseHeadersLength; i++) {
//                    String headerName = headers.name(i);
//                    String headerValue = headers.get(headerName);
//                    headerMap.put(headerName, headerValue);
//                }
//                Gson gson = new Gson();
//                // 得到header的JSON形式
//                String headersJson = gson.toJson(headerMap);
//                if(response.code() == 200) {
//                    ResponseBody body = response.body();
//                    // 获取文件总长度
//                    final long totalLength = body.contentLength();
//                    //以流的方式进行读取
//                    try {
//                        InputStream inputStream = body.byteStream();
//                        FileOutputStream outputStream = new FileOutputStream(targetFile);
//                        byte[] buffer = new byte[2048];
//                        int len = 0;
//                        int num = 0;
//                        while ((len = inputStream.read(buffer)) != -1){
//                            num+=len;
//                            outputStream.write(buffer,0,len);
//                            final int finalNum = num;
//                            Log.e(TAG, "下载进度 == " + finalNum *100/totalLength);
////                        handler.post(new Runnable() {
////                            @Override
////                            public void run() {
////                                callBack.onProgressBar( finalNum *100/totalLength);
////                            }
////                        });
//                        }
//                        //读取完关闭流
//                        outputStream.flush();
//                        if(inputStream != null) {
//                            inputStream.close();
//                        }
//                        if(outputStream != null) {
//                            outputStream.close();
//                        }
////                        String strBody = response.body().string();
//                        activity.asyncRequestCallBack((String) map.get("success"), "body", headersJson, String.valueOf(response.code()));
//                    }catch (Exception e) {
//                        Log.e(TAG, "下载错误信息 == " + e.toString());
//                        activity.asyncRequestCallBack((String) map.get("failed"), e.getMessage(), headersJson, String.valueOf(response.code()));
//                        e.printStackTrace();
//                    }
//
//                }else {
//                    String strBody = response.body().string();
//                    activity.asyncRequestCallBack((String) map.get("failed"), strBody, headersJson, String.valueOf(response.code()));
//                }
//            }
//        });
    }


    /**
     * 同步线程
     * @param moduleInfo
     */
    public static void downLoad(MixModuleInfo moduleInfo, String fileName, String savePath) {
//        // zip文件下载后保存到的文件夹名
//        String zipParentFileName = identifier.substring(identifier.lastIndexOf(".") + 1, identifier.length());
//        // zip文件下载后保存的完整路径
//        String outputFile = baseFileUrl + SystemInfo.getApplicationPackageName(getActivity()) + "/ext/" + zipParentFileName;
//        // zip文件下载后的文件名称  如：zwt-ext-attendance.zip， 取自 "url": "http://192.168.11.129:4053/zwt-ext/zwt-ext-attendance.zip",
//        String zipUrl = been.getUrl();
//        String zipFileName = zipUrl.substring(zipUrl.lastIndexOf("/") + 1, zipUrl.length());
        URL url = null;
        try {
            url = new URL(moduleInfo.getMixModuleURL());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);
            //文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("Activity", "下载错误 === " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Activity", "下载错误 === " + e.toString());
        }
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    public interface RequestCallBack {
        void callBack(String str);
    }
}
