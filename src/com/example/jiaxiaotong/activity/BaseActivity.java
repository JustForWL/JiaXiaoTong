package com.example.jiaxiaotong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Base activity copied from yixin.com
 * @author yixin.com
 *
 */
public class BaseActivity extends FragmentActivity {
	
	/**
	 * 通过类名启动Activity
	 * @param pClass
	 */
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
		this.finish();
	}
	
	/**
	 * 通过类名启动Activity, 并且含有Bundle数据
	 * @param pClass
	 * @param pBundle
	 */
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
		this.finish();
	}
	
	/**
	 * 通过Action名启动Activity
	 * @param pAction
	 */
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}
    
	/**
	 * 通过Action名启动Activity，并且含有Bundle数据
	 * @param pClassName
	 * @param pBundle
	 */
	protected void openActivity(String pClassName, Bundle pBundle) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClassName("com.example.jiaxiaotong.activity", pClassName);
		if (pBundle != null){
			intent.putExtras(pBundle);
		}
		startActivity(intent);
		this.finish();
	}
}
