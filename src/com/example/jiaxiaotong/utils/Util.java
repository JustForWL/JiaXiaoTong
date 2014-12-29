package com.example.jiaxiaotong.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jivesoftware.smack.packet.Message;

import com.example.jiaxiaotong.R;
import com.example.jiaxiaotong.activity.ChatActivity;
import com.example.jiaxiaotong.activity.MultiChatActivity;
import com.example.jiaxiaotong.constants.App;
import com.example.jiaxiaotong.dao.ChildDB;
import com.example.jiaxiaotong.service.ReceiverService;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;

public class Util{
	public static BitmapDrawable getBackground(String iconAddr) {
		// TODO Auto-generated method stub
		BitmapDrawable bitmapDrawable = null;
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String filePath = Environment.getExternalStorageDirectory().getPath() + 
					"/jiaxiaotong/icon/" + iconAddr;
			File file = new File(filePath);
			if(file.canRead()) {
				bitmapDrawable = (BitmapDrawable) BitmapDrawable.createFromPath(filePath);
			}
		}
		return bitmapDrawable;
	}
	
	public static String formatDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	
	public static List removeDuplicateWithOrder(List list) {
		Set set = new HashSet();
		List newList = new ArrayList();
		for(Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if(set.add(element)) {
				newList.add(element);
			}
		}
		return newList;
	}
	
	public static void showNotify(Context context, Message message, String userName, 
			int num, boolean isMulti, String className)
	{
		
		// 更新通知栏
		NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

		int icon = R.drawable.copyright;
		CharSequence tickerText = null;
		if(0 == num) {
			tickerText = userName + ":"
				+ message.getBody().split("@", 2)[1];
		}else {
			tickerText = "[" + num + "]" + 
					userName + ":"+ message.getBody().split("@", 2)[1];
		}
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		//notification.flags = Notification.FLAG_NO_CLEAR;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		// 设置默认声音
		notification.defaults |= Notification.DEFAULT_SOUND;
		// 设定震动(需加VIBRATE权限)
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.contentView = null;
        if(!isMulti) {
			Intent intent = new Intent(context, ChatActivity.class);
			Bundle bundle = new Bundle();
			String fromAccount = message.getFrom();
			fromAccount = fromAccount.substring(0, fromAccount.indexOf("@"));
			bundle.putString(App.TALK, userName);
			bundle.putString(App.ACCOUNT, fromAccount);
			intent.putExtras(bundle);
			// 当点击通知时，我们让原有的Activity销毁，重新实例化一个
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent contentIntent = PendingIntent.getActivity(context, App.NotificationId,
					intent, PendingIntent.FLAG_CANCEL_CURRENT);
			notification.setLatestEventInfo(context,
					userName + " 有新消息",
					tickerText, contentIntent);
        }else {
        	Intent intent = new Intent(context, MultiChatActivity.class);
			Bundle bundle = new Bundle();
			String from = message.getFrom();
			String fromAccount = from.substring(from.indexOf("/") + 1, from.length() - 1);
			ChildDB cdb = new ChildDB(context);
			String currentChild = SharePreferencesUtil.readCurrentChild(context);
			if(className.equals(cdb.getClassName(currentChild))) {
				bundle.putString(App.CHILDNAME, currentChild);
			}else {
				bundle.putString(App.CHILDNAME, cdb.getChildName(className));
			}
			bundle.putString(App.CLASSNAME, className);
			bundle.putString(App.ACCOUNT, fromAccount);
			intent.putExtras(bundle);
			// 当点击通知时，我们让原有的Activity销毁，重新实例化一个
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent contentIntent = PendingIntent.getActivity(context, App.NotificationId,
					intent, PendingIntent.FLAG_CANCEL_CURRENT);
			notification.setLatestEventInfo(context,
					className + " 有新消息",
					tickerText, contentIntent);
        }
		 nm.notify(App.NotificationId++, notification);// 通知一下才会生效哦
		 App.NotificationId++;
	}

}
