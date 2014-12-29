package com.example.jiaxiaotong.activity;

import org.jivesoftware.smack.XMPPConnection;

import com.example.jiaxiaotong.R;
import com.example.jiaxiaotong.constants.AllTypes;
import com.example.jiaxiaotong.constants.App;
import com.example.jiaxiaotong.service.ReceiverService;
import com.example.jiaxiaotong.utils.LoadingDialog;
import com.example.jiaxiaotong.utils.Logger;
import com.example.jiaxiaotong.utils.XMPPManager;
import com.example.jiaxiaotong.utils.NetUtil;
import com.example.jiaxiaotong.utils.SharePreferencesUtil;
import com.example.jiaxiaotong.utils.T;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class LoginActivity extends BaseActivity implements OnClickListener{
	
	private TextView userName = null;
	private TextView userPass = null;
	private Button loginBtn = null;
	private LoadingDialog loadingDialog = null;
	private Handler handler = new Handler();
	private Runnable connectionTimeoutCallBack = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (loadingDialog != null) {
				loadingDialog.dismiss();
				T.showShort(LoginActivity.this, "登录超时，请重试");
			}
		}
		
	};
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
    }
	
	public void initView() {
		userName = (TextView) findViewById(R.id.login_user_edit);
		userPass = (TextView) findViewById(R.id.login_passwd_edit);
		loginBtn = (Button) findViewById(R.id.login_login_btn);
		loginBtn.setOnClickListener(this);
		loadingDialog = new LoadingDialog();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String userAccount = userName.getText().toString();
		String userPassword = userPass.getText().toString();
		if(userAccount.equals("") || userPassword.equals("")){
			T.showLong(this, "用户名或密码不能为空");
			return;
		}else{
			Thread loginServiceThread = new Thread(new LoginThread(userAccount, userPassword));
			loginServiceThread.start();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		this.loadingDialog = null;
		super.onDestroy();
	}
	
	@Override
	protected void onStop() {
		this.loadingDialog = null;
		super.onStop();
	}
	class LoginThread implements Runnable{
		
		private String userAccount;
		private String userPassword;
		
		public LoginThread(String userName, String userPass) {
			this.userAccount = userName;
			this.userPassword = userPass;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(!NetUtil.isNetConnected(LoginActivity.this)){
				T.showShort(LoginActivity.this, R.string.net_disconnect_hint);
				//return;
			} else {
				handler.postDelayed(connectionTimeoutCallBack, App.TIME_DELAY_LOADING);
				loadingDialog.show(getFragmentManager(), "LOAD_DIALOG");
				loadingDialog.setCancelable(false);
				try{
					XMPPManager myConnection = XMPPManager.getInstance();
					String loginState = myConnection.isLogin(userAccount, userPassword);
					if(loginState.equals("SUCCESS")) {
						SharePreferencesUtil.writeIsFirstLogin(LoginActivity.this);
						SharePreferencesUtil.writeLoginAccount(LoginActivity.this, userAccount);
						if(userAccount.startsWith("p")) {
							new Thread(new ParseLoginParam()).start();
							openActivity(ParentFrame.class);
							SharePreferencesUtil.writeLoginType(LoginActivity.this, AllTypes.PARENTS.toString());
						}else if(userAccount.startsWith("t")) {
							openActivity(TeacherFrame.class);
							SharePreferencesUtil.writeLoginType(LoginActivity.this, AllTypes.TEACHERS.toString());
						}else if(userAccount.startsWith("h")){
							openActivity(HeadTeacherFrame.class);
							SharePreferencesUtil.writeLoginType(LoginActivity.this, AllTypes.HEADETEACHER.toString());
						}
						Intent service = new Intent();
						service.setAction("com.example.jiaxiaotong.service.ReceiverService");
						startService(service);
					 }else{
						T.showLong(LoginActivity.this, "无效的用户名");
						return;
					}
					LoginActivity.this.finish();
				} catch(Exception e){
					e.printStackTrace();
					Logger.i("登录失败");
					return;
				}
			}
		}
		
	}
	//登录服务器，解析服务器返回的json数据，并存储相应的数据
	class ParseLoginParam implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
