package com.qupp.client.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.MainActivity;
import com.qupp.client.ui.view.activity.main.CommodityList;
import com.qupp.client.ui.view.activity.main.H5Web;
import com.qupp.client.ui.view.activity.main.NewCommodityDetailsActivity;
import com.qupp.client.ui.view.activity.mine.order.OrderDetails;
import com.qupp.client.ui.view.activity.scoreshop.CommodityDetailsActivity;
import com.qupp.client.network.bean.PushBean;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

import static android.content.Context.NOTIFICATION_SERVICE;

public class PushMessageReceiver extends JPushMessageReceiver {
    private static final String TAG = "PushMessageReceiver";


    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.e(TAG, "[onMessage] " + customMessage);
        processCustomMessage(context, customMessage);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageOpened] " + message);

        Log.d("onNotifyMessageOpened", message.notificationExtras);
        try {
            JSONObject jsonObject = new JSONObject(message.notificationExtras);
            String jsonstr = jsonObject.getString("jiayikou");

            PushBean bean = new Gson().fromJson(jsonstr, PushBean.class);

            String type = bean.getKey();
            String linkId =bean.getValue();
            String h5id =bean.getH5Id();

            Log.d("onNotifyMessageOpened", type+","+linkId+","+h5id);

        /*    String type = jsonObject.keys().next();
            String linkId = jsonObject.getString(jsonObject.keys().next());*/
            try {
                //打开自定义的Activity
                Intent i = new Intent(context, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, message.notificationTitle);
                bundle.putString(JPushInterface.EXTRA_ALERT, message.notificationContent);
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
                if (type.equals("11")) {
                    OrderDetails.startActivityInstance1(context,linkId,"0");
                }else if(type.equals("12")){
                    NewCommodityDetailsActivity.startActivityInstance1(context,"",linkId,"","");
                }else if(type.equals("1")){
                    //h5
                    H5Web.startActivityInstance(context, h5id,linkId);
                }else if(type.equals("2")){
                    //拍卖商品
                    CommodityList.startActivityInstance1(context,"商品",linkId);
                }else if(type.equals("3")){
                    //积分商城详情
                    CommodityDetailsActivity.startActivityInstance1(context, linkId);
                }
            } catch (Throwable throwable) {
                Log.d("onNotifyMessageOpened", throwable.getMessage());
            }



        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("onNotifyMessageOpened", e.getMessage());
        }



    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        Log.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");
        String nActionExtra = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            Log.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null");
            return;
        }
        if (nActionExtra.equals("my_extra1")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一");
        } else if (nActionExtra.equals("my_extra2")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二");
        } else if (nActionExtra.equals("my_extra3")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三");
        } else {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义");
        }
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageArrived] " + message);

        Gson gson = new Gson();
       /* EntityForPush pushEntity = gson.fromJson(message.notificationExtras, EntityForPush.class);


            if(!MyApplication.getOrderId().contains(pushEntity.getOrderId())){
                //不包含
                MyApplication.setOrderId(pushEntity.getOrderId());
                sendChatMsg(context,pushEntity.getTitle(),pushEntity.getContent());
                speak(context,pushEntity.getContent());
                //刷新数据
                EventBus.getDefault().post(new ExitRefresh());
            }*/


        //speak(context,message.notificationContent);
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageDismiss] " + message);
    }

    @Override
    public void onRegister(Context context, String registrationId) {
        Log.e(TAG, "[onRegister] " + registrationId);
    }

    @Override
    public void onConnected(Context context, boolean isConnected) {
        Log.e(TAG, "[onConnected] " + isConnected);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.e(TAG, "[onCommandResult] " + cmdMessage);
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, CustomMessage customMessage) {
      /*  //透传
        Gson gson = new Gson();
        EntityForPush pushEntity = gson.fromJson(customMessage.extra, EntityForPush.class);
        sendChatMsg(context,pushEntity.getTitle(),pushEntity.getContent());
        speak(context,pushEntity.getContent());

        //刷新数据
        EventBus.getDefault().post(new ExitRefresh());*/
    }


    public void sendChatMsg(Context context, String title, String content) {
        String channelId = "chat";
        String channelName = "重要消息";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            //8.0
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(context, "chat")
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true)
                    .build();
            manager.notify(1, notification);
        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher).setContentTitle(title)// 标题
                    .setContentText(content)// 内容
                    // .setVibrate(new long[] { 100, 400, 100, 400 })// 震动
                    .setLights(0xff0000ff, 300, 0).
                            setDefaults(Notification.DEFAULT_SOUND)
                    .setPriority(Notification.PRIORITY_MAX);// 设置优先级;
            mBuilder.setTicker("有新消息");// 第一次提示消息的时候显示在通知栏上
            // mBuilder.setNumber(12);
            Bitmap btm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);// 图标
            mBuilder.setLargeIcon(btm);
            mBuilder.setAutoCancel(true);// 自己维护通知的消失
            mBuilder.setSmallIcon(R.drawable.ic_launcher);
            // 构建一个Intent
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            // 设置通知主题的意图
            mBuilder.setContentIntent(resultPendingIntent);
            // 获取通知管理器对象
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }
    }

}
