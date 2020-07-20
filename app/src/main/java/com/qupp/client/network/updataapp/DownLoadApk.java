package com.qupp.client.network.updataapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.qupp.client.utils.MySharePerference;

import java.io.File;

public class DownLoadApk {
    public static final String TAG = DownLoadApk.class.getSimpleName();

    public static void download(Context context, String url, String title, final String appName) {
        // 获取存储ID
        long downloadId =  MySharePerference.getShardPerferienceLong(context,DownloadManager.EXTRA_DOWNLOAD_ID,-1L);
        if (downloadId != -1L) {
            FileDownloadManager fdm = FileDownloadManager.getInstance(context);
            int status = fdm.getDownloadStatus(downloadId);
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                //启动更新界面
                Uri uri = Uri.parse(fdm.getDownloadPath(downloadId));
                if (uri != null) {
                    if (compare(getApkInfo(context, uri.getPath()), context)) {
                        //startInstall(context, uri);
                       // Toast.makeText(context,"111",Toast.LENGTH_LONG).show();
                        startInstall(context,fdm.getDownloadPath(downloadId));
                        return;
                    } else {
                        //Toast.makeText(context,"222",Toast.LENGTH_LONG).show();
                        fdm.getDownloadManager().remove(downloadId);
                    }
                }
                start(context, url, title,appName);
            } else if (status == DownloadManager.STATUS_FAILED) {
                fdm.getDownloadManager().remove(downloadId);
                start(context, url, title,appName);
            } else {
                fdm.getDownloadManager().remove(downloadId);
                start(context, url, title,appName);
            }
        } else {
            start(context, url, title,appName);
        }
    }

    private static void start(Context context, String url, String title, String appName) {
        Toast.makeText(context,"正在下载...",Toast.LENGTH_LONG).show();
        long id = FileDownloadManager.getInstance(context).startDownload(url, title, "下载完成后点击打开",appName);
        MySharePerference.addShareLongPerference(context,DownloadManager.EXTRA_DOWNLOAD_ID, id);
        Log.d(TAG, "apk start download " + id);
    }

    public static void startInstall(Context context, String filepath) {
       /* Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);*/

        // 这里是APK路径
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            Intent intent2 = new Intent();
            intent2.setAction(android.content.Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(context, "com.weisheng.coinla.fileprovider", new File(filepath));
            intent2.setDataAndType(uri, "application/vnd.android.package-archive");
            intent2.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent2);
        }else{
            Intent intent2 = new Intent();
            intent2.setAction(android.content.Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(new File(filepath));
            intent2.setDataAndType(uri, "application/vnd.android.package-archive");
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
        }
    }


    /**
     * 获取apk程序信息[packageName,versionName...]
     *
     * @param context Context
     * @param path    apk path
     */
    private static PackageInfo getApkInfo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            return info;
        }
        return null;
    }


    /**
     * 下载的apk和当前程序版本比较
     *
     * @param apkInfo apk file's packageInfo
     * @param context Context
     * @return 如果当前应用版本小于apk的版本则返回true
     */
    private static boolean compare(PackageInfo apkInfo, Context context) {
        if (apkInfo == null) {
            return false;
        }
        String localPackage = context.getPackageName();
        if (apkInfo.packageName.equals(localPackage)) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(localPackage, 0);
                if (apkInfo.versionCode > packageInfo.versionCode && !apkInfo.versionName.equals(packageInfo.versionName)) {
                    return true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}