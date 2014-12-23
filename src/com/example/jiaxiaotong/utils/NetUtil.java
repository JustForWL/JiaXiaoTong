package com.example.jiaxiaotong.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判断网络是否通畅
 * 
 *
 */
public class NetUtil {
	public static boolean isNetConnected(Context context) {
		boolean isNetConnected;
		// 获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
//			String name = info.getTypeName();
//			Logger.i("当前网络名称：" + name);
			isNetConnected = true;
		} else {
			Logger.i("没有可用网络");
			isNetConnected = false;
		}
		return isNetConnected;
	}
}
