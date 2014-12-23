package com.example.jiaxiaotong.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.ViewConfiguration;
import android.view.Window;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;

/**
 * Base frame for all frame
 * @author Arthur
 *
 */
public class BaseFrame extends FragmentActivity implements TabListener{
    public ActionBar actionBar = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		setOverflowShowingAlways();
	}
	
	public ActionBar configActionBar() {
		ActionBar actionBar = getActionBar();
		assert(actionBar != null);
		actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		return actionBar;
	}
	
    
	public void initView() {
		actionBar = configActionBar();
		
	}
	private void setOverflowShowingAlways() {
		try{
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		if(featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if(menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try{
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", 
							Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
    
}
