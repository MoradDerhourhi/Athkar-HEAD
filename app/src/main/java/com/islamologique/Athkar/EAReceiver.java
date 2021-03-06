package com.islamologique.Athkar;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class EAReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            new EANotificationManager().setAlarmIfNeeded(context);
            return;
        }else if(intent.getAction()!=null && intent.getAction().equals("clear")){
            new EANotificationManager().clearNotification(context);
            return;
        }

        new EANotificationManager().showNotification(context);



    }



}