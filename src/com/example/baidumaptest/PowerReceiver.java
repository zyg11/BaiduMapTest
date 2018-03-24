package com.example.baidumaptest;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//public class PowerReceiver extends BroadcastReceiver {
//
//	@SuppressLint("Wakelock")
//    @Override
//    public void onReceive(final Context context, final Intent intent) {
//        final String action = intent.getAction();
//
//        //按下电源键,关闭屏幕
//        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
//            System.out.println("screen off,acquire wake lock!");
//            if (null != MainActivity.wakeLock && !(MainActivity.wakeLock.isHeld())) {
//                MainActivity.wakeLock.acquire();
//            }
//         //按下电源键,打开屏幕  
//        } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
//            System.out.println("screen on,release wake lock!");
//            if (null != MainActivity.wakeLock && MainActivity.wakeLock.isHeld()) {
//                MainActivity.wakeLock.release();
//            }
//        }
//    }
//
//}
