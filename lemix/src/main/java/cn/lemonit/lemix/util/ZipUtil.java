package cn.lemonit.lemix.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {

    /**
     * 解压zip
     * @param zipFilePath  要解压的zip文件的路径
     * @param strPath   解压后要放的位置
     * @throws IOException
     */
    public static boolean unZip(String zipFilePath, String strPath) throws IOException {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            Log.e("ZipUtil", "zipFilePath == " + zipFilePath);
            String gbkPath, strtemp;
//            strPath = targetFile.getAbsolutePath();
            Enumeration<?> e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry zipEnt = (ZipEntry) e.nextElement();
                gbkPath = zipEnt.getName();
                if (zipEnt.isDirectory()) {
                    strtemp = strPath + File.separator + gbkPath;
                    File dir = new File(strtemp);
                    dir.mkdirs();
                    continue;
                } else {
                    // 读写文件
                    InputStream is = zipFile.getInputStream(zipEnt);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    gbkPath = zipEnt.getName();
                    strtemp = strPath + File.separator + gbkPath;
                    // 建目录
                    String strsubdir = gbkPath;
                    for (int i = 0; i < strsubdir.length(); i++) {
                        if (strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
                            String temp = strPath + File.separator + strsubdir.substring(0, i);
                            File subdir = new File(temp);
                            if (!subdir.exists())
                                subdir.mkdir();
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(strtemp);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int c;
                    while ((c = bis.read()) != -1) {
                        bos.write((byte) c);
                    }
                    bos.close();
                    fos.close();
                }
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            Log.e("Activity", "---解压错误: " + e.toString());
        }
        return false;
    }

    /**
     * 解压zip
     * @param zipFile_  zip文件
     * @param targetFile   解压后要放的位置
     * @throws IOException
     */
    public static boolean unZip(File zipFile_, File targetFile) throws IOException {
        try {
            String zipFilePath = zipFile_.getPath();
            ZipFile zipFile = new ZipFile(zipFilePath);
            Log.e("ZipUtil", "zipFilePath == " + zipFilePath);
            String strPath, gbkPath, strtemp;
            strPath = targetFile.getAbsolutePath();
            Enumeration<?> e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry zipEnt = (ZipEntry) e.nextElement();
                gbkPath = zipEnt.getName();
                if (zipEnt.isDirectory()) {
                    strtemp = strPath + File.separator + gbkPath;
                    File dir = new File(strtemp);
                    dir.mkdirs();
                    continue;
                } else {
                    // 读写文件
                    InputStream is = zipFile.getInputStream(zipEnt);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    gbkPath = zipEnt.getName();
                    strtemp = strPath + File.separator + gbkPath;
                    // 建目录
                    String strsubdir = gbkPath;
                    for (int i = 0; i < strsubdir.length(); i++) {
                        if (strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
                            String temp = strPath + File.separator + strsubdir.substring(0, i);
                            File subdir = new File(temp);
                            if (!subdir.exists())
                                subdir.mkdir();
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(strtemp);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int c;
                    while ((c = bis.read()) != -1) {
                        bos.write((byte) c);
                    }
                    bos.close();
                    fos.close();
                }
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            Log.e("Activity", "---解压错误");
        }
        return false;
    }



    /**
     * 解压zip
     * @param context
     * @param zipFile_  zip文件
     * @param targetFile   解压后要放的位置
     * @throws IOException
     */
    public static void unZip(Context context, File zipFile_, File targetFile, ZipCallBack callBack) throws IOException {
        try {
            String zipFilePath = zipFile_.getPath();
            ZipFile zipFile = new ZipFile(zipFilePath);
            Log.e("ZipUtil", "zipFilePath == " + zipFilePath);
            String strPath, gbkPath, strtemp;
            strPath = targetFile.getAbsolutePath();
            Enumeration<?> e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry zipEnt = (ZipEntry) e.nextElement();
                gbkPath = zipEnt.getName();
                if (zipEnt.isDirectory()) {
                    strtemp = strPath + File.separator + gbkPath;
                    File dir = new File(strtemp);
                    dir.mkdirs();
                    continue;
                } else {
                    // 读写文件
                    InputStream is = zipFile.getInputStream(zipEnt);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    gbkPath = zipEnt.getName();
                    strtemp = strPath + File.separator + gbkPath;
                    // 建目录
                    String strsubdir = gbkPath;
                    for (int i = 0; i < strsubdir.length(); i++) {
                        if (strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
                            String temp = strPath + File.separator + strsubdir.substring(0, i);
                            File subdir = new File(temp);
                            if (!subdir.exists())
                                subdir.mkdir();
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(strtemp);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int c;
                    while ((c = bis.read()) != -1) {
                        bos.write((byte) c);
                    }
                    bos.close();
                    fos.close();
                }
            }
            callBack.callBack();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ZipCallBack {
        void callBack();
    }
}
