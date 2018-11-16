package cn.lemonit.lemix.util;

import java.io.File;

import cn.lemonit.lemix.mixmodule.MixModuleInfo;

public class FileUtil {

    /**
     * 查找文件
     * @return  文件名称
     */
    public static String findFile(String path, MixModuleInfo moduleInfo) {
        String fileName = null;
        File fileParent = new File(path);
        if(!fileParent.exists()) {
            return fileName;
        }
        File[] files = fileParent.listFiles();
        for(File file : files) {
            if(file.getName().startsWith(moduleInfo.getMixModuleIdentifier() + "-")) {
                return file.getName();
            }
        }
        return fileName;
    }

    /**
     * 删除文件夹以及文件
     */
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();
        } else if (file.exists()) {
            file.delete();
        }
    }
}
