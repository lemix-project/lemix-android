package cn.lemonit.lemix.util;

import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ScanFile {

    /**
     * 扫描
     * @param scanFile  需要扫描的目标文件夹
     * @param fileName  要找的目标文件名称
     * @return
     */
    public static void scanFile(File scanFile, String fileName, String key, Map<String, String> map) {
        File[] files = scanFile.listFiles();
        if(files == null || files.length < 1) return;
        for(File fileChild : files) {
            // 如果是文件夹，递归
            if(fileChild.isDirectory()) {
                scanFile(fileChild, fileName, key, map);
            }
            // 如果是文件，就判断是不是目标文件
            else {
                // 是目标文件，就保存到map中
                if(fileChild.getName().equals(fileName)) {
//                    String key = been.getConfig().getIdentifier() + "." + scanFile.getName();
                    String key_ = key + "/" + scanFile.getName();
                    String value = fileChild.getAbsolutePath();
                    map.put(key_, value);

                }
            }
        }
    }
}
