package com.qupp.client.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期处理工具类
 * author：wangqi on 2017/4/26 17:48
 * email：773630555@qq.com
 */
public class DateUtils {

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param seconds
     * @return
     */
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }
    /**
     * 日期格式字符串转换成时间戳
     * @param date_str 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str,String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime()/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }





    /**
     * 字符串时间转字符串时间
     * @param date_str
     * @param format yyyy-MM-dd HH:mm:ss 原
     * @param format1 yyyy-MM-dd HH:mm:ss 目标
     * @return
     */
    public static String dateToDate(String date_str,String format,String format1){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            SimpleDateFormat sdf1 = new SimpleDateFormat(format1);
            return sdf1.format(new Date(sdf.parse(date_str).getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * -1是昨天，0是今天，1是明天依次类推
     * @param i
     * @return
     */
    public static String getStartTime(int i) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String dateString="";
        try {
            calendar.add(calendar.DATE,i);//把日期往后增加一天.整数往后推,负数往前移动
            date=calendar.getTime(); //这个时间就是日期往后推一天的结果
            dateString = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;


    }

    /**
     * -1是昨天，0是今天，1是明天依次类推
     * @param i
     * @return
     */
    public static String getEndtime(int i) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String dateString="";
        try {
            calendar.add(calendar.DATE,i);//把日期往后增加一天.整数往后推,负数往前移动
            date=calendar.getTime(); //这个时间就是日期往后推一天的结果
            dateString = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;


    }


    public static String getDistanceTime(long diff) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        hour = (diff / (60 * 60 * 1000));
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        String strh = "";
        String strm = "";
        String strS = "";
        if(hour<10){
            strh = "0"+hour;
        }else{
            strh = hour+"";
        }
        if(min<10){
            strm = "0"+min;
        }else{
            strm = min+"";
        }
        if(sec<10){
            strS = "0"+sec;
        }else{
            strS = sec+"";
        }
        if(strS.contains("-")){
            strS = "00";
        }
        return strh+":"+strm+":"+strS;
    }

    public static String getDistanceTime1(long diff) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day != 0) return day + "天"+hour + "小时";
        if (hour != 0) return hour +"小时";
        if (min != 0) return "刚寄拍";
        if (sec != 0) return "刚寄拍";
        min = ((diff / (60)));
        return min + "";
    }

    public static String getDistanceTime2(long diff) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String strh = "";
        String strm = "";
        String strS = "";
        if(hour<10){
            strh = "0"+hour;
        }else{
            strh = hour+"";
        }
        if(min<10){
            strm = "0"+min;
        }else{
            strm = min+"";
        }
        if(sec<10){
            strS = "0"+sec;
        }else{
            strS = sec+"";
        }

        if (day != 0) return day + "天"+strh + "小时";
        if (hour != 0) return strh + "小时"+ strm + "分"+strS+"秒";
        if (min != 0) return strm + "分"+strS+"秒";
        if (sec != 0) return strS+"秒";
        min = ((diff / (60)));
        return "";
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getNowTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }


}