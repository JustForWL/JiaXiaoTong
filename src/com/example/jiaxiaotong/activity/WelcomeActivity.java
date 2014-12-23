package com.example.jiaxiaotong.activity;

import com.example.jiaxiaotong.R;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import com.example.jiaxiaotong.constants.App;
import com.example.jiaxiaotong.constants.AllTypes;
import com.example.jiaxiaotong.utils.SharePreferencesUtil;

/**
 *  引导Activity,用来判断应当跳转到哪个Activity 
 * @author arthur
 *
 */
public class WelcomeActivity extends BaseActivity {
	
	public Context mContext = null;
    //程序是否登录过了
	private boolean isLogin;
	//登录用户的类型
	private String loginType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		mContext = WelcomeActivity.this;
		isLogin = SharePreferencesUtil.readIsFirstLogin(mContext);
		if (!isLogin) { //未登录就跳转到登录Activity
			new Handler().postDelayed(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					openActivity(LoginActivity.class);
				}
				
			},
			App.TIME_DELAY_ACTIVITY);
		}else{
			loginType = SharePreferencesUtil.readLoginType(mContext);
			if(loginType.equals("ParentFrame")){
				new Handler().postDelayed(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						openActivity(ParentFrame.class);
					}
					
				}, 
				App.TIME_DELAY_ACTIVITY);
			}else if(loginType.equals("TeacherFrame")){
				new Handler().postDelayed(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						openActivity(TeacherFrame.class);
					}
					
				}, 
				App.TIME_DELAY_ACTIVITY);
			} else {
				new Handler().postDelayed(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						openActivity(HeadTeacherFrame.class);
					}
					
				}, 
				App.TIME_DELAY_ACTIVITY);
			}				
		}
		
	}
}
