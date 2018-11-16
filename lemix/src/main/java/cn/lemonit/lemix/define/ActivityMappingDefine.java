package cn.lemonit.lemix.define;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ActivityMappingDefine {

    private static final String PACKAGE_BASE = "com.zhongwang.zwt.activity.%s";
    public static Map<String, Class> mapping;

    public static Map<String, Class> getMapping(Context mContext) {
        if (mapping == null){
//            String content = "{}";
            String content = getJson("activity_mapping.json", mContext);
            Gson gson = new Gson();
            Map<String,String> strMapping = gson.fromJson(content, Map.class);
            mapping = new HashMap<>();
            for (String key : strMapping.keySet()){
                try {
                    mapping.put(key, Class.forName(String.format(PACKAGE_BASE, strMapping.get(key))));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return mapping;
    }

    private static String getJson(String fileName, Context context) {
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
