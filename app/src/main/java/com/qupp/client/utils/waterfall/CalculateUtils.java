package com.qupp.client.utils.waterfall;

import com.qupp.client.MyApplication;


/**
 * 计算高度工具类
 */
public class CalculateUtils {

    public static int getHeightByUrl(String url){
        int fianlHeight;
        String urlstr = url.substring(url.indexOf("?")+1);
        String[]  urlaar = urlstr.split("\\/");
        String whStr="";
        if(urlaar!=null&&urlaar.length>0){
            whStr = urlaar[0];
        }
        if(whStr.contains("*")){
            String[] wh = whStr.split("\\*");
            Double  hwScale = Double.valueOf(wh[1])/ Double.valueOf(wh[0]);
            fianlHeight = (int) (MyApplication.itemHeight*hwScale);
        }else{
            //原始比例1：1
            fianlHeight = MyApplication.itemHeight;
        }
        if(fianlHeight>(MyApplication.itemHeight)*2){
            fianlHeight=(MyApplication.itemHeight)*2;
        }
        return fianlHeight;
    }

}