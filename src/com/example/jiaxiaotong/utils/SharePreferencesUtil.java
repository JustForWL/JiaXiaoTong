package com.example.jiaxiaotong.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.jiaxiaotong.constants.App;

/**
 * sharePreference 工具
 * @author arthur
 *
 */

public class SharePreferencesUtil {
	
	public static SharedPreferences getSharedPreferences(Context context, String appName) {
		SharedPreferences preferences = context.getSharedPreferences(
				App.APP_NAME, Context.MODE_PRIVATE);
		return preferences;
	}
	public static boolean readIsFirstLogin(Context context) {
		SharedPreferences preferences = getSharedPreferences(context, App.APP_NAME);
		if (preferences == null)
			return false;
		boolean isLogin = preferences.getBoolean(App.IS_LOGIN, false);
		return isLogin;
	}
	
	public static boolean writeIsFirstLogin(Context context) {
		SharedPreferences preferences = getSharedPreferences(context, App.APP_NAME);
		if (preferences == null)
			return false;
		Editor editor = preferences.edit();
		editor.putBoolean(App.IS_LOGIN, true);
		editor.commit();
		return true;
	}
	
	public static String readLoginType(Context context) {
		SharedPreferences preferences = getSharedPreferences(context, App.APP_NAME);
		if (preferences == null)
			return "";
		String loginType = preferences.getString(App.LOGIN_TYPE, "");
		return loginType;
	}
	
	public static boolean writeLoginType(Context context, String loginType) {
		SharedPreferences preferences = getSharedPreferences(context, App.APP_NAME);
		if (preferences == null)
			return false;
		Editor editor = preferences.edit();
		editor.putString(App.LOGIN_TYPE, loginType);
		editor.commit();
		return true;
	}
	
	public static boolean writeLoginAccount(Context context, String loginAccount) {
		SharedPreferences preferences = getSharedPreferences(context, App.APP_NAME);
		if (preferences == null)
			return false;
		Editor editor = preferences.edit();
		editor.putString(App.LOGIN_ACCOUNT, loginAccount);
		editor.commit();
		return true;
	}
	
	public static String readLoginAccount(Context context) {
		SharedPreferences preferences = getSharedPreferences(context, App.APP_NAME);
		if (preferences == null)
			return "";
		String loginAccount = preferences.getString(App.LOGIN_ACCOUNT, "");
		return loginAccount;
	}
	
	public static boolean writeCurrentChild(Context context, String currentChild) {
		SharedPreferences preferences = getSharedPreferences(context, App.APP_NAME);
		if (preferences == null)
			return false;
		Editor editor = preferences.edit();
		editor.putString(App.CURRENTCHILD, currentChild);
		editor.commit();
		return true;
	}
	
	public static String readCurrentChild(Context context) {
		SharedPreferences preferences = getSharedPreferences(context, App.APP_NAME);
		if (preferences == null)
			return "";
		String loginAccount = preferences.getString(App.CURRENTCHILD, "");
		return loginAccount;
	}
}
