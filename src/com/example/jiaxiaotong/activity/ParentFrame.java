package com.example.jiaxiaotong.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


import com.example.jiaxiaotong.R;
import com.example.jiaxiaotong.fragment.InfoFragment;
import com.example.jiaxiaotong.fragment.KidFragment;
import com.example.jiaxiaotong.fragment.StuFragment;
import com.example.jiaxiaotong.fragment.TchFragment;
import com.example.jiaxiaotong.utils.Logger;
import android.app.ActionBar.TabListener;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;


public class ParentFrame extends BaseFrame {
    
	public static final String[] TABS = {"公告信息", "班级学生", "教师信息", "我的孩子"};
    public static final int MAX_TAB_SIZE = 4;
    private ViewPager mViewPager = null;
    private TabFragmentPagerAdapter mAdapter = null;
    
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_parent_frame);
        super.onCreate(savedInstanceState);     
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prarent_frame, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.action_refresh:

                return true;
            case R.id.action_search:

                return true;

        }

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
    
    public void initView() {
    	super.initView();
    	mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
    	mViewPager = (ViewPager)findViewById(R.id.pager);
    	mViewPager.setAdapter(mAdapter);
    	mViewPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				//actionBar.setSelectedNavigationItem(arg0);
			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				actionBar.setSelectedNavigationItem(arg0);
			}
    		
    	});
    	
    	for(int i=0;i<MAX_TAB_SIZE;i++){  
            Tab tab = actionBar.newTab();  
            tab.setText(mAdapter.getPageTitle(i)).setTabListener(this);
            tab.setTabListener(this);
            actionBar.addTab(tab);  
        }  
    	actionBar.setSelectedNavigationItem(0);
    	mViewPager.setCurrentItem(0);
    }
    
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		//actionBar.setSelectedNavigationItem(tab.getPosition());
		//Logger.i(tab.getPosition()+"click");
		mViewPager.setCurrentItem(tab.getPosition());
	}


	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		super.onTabUnselected(tab, ft);
	}


	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		super.onTabReselected(tab, ft);
	}

	public static class TabFragmentPagerAdapter extends FragmentPagerAdapter{
		
		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			Fragment fragment = null;
			switch(arg0){
			case 0:
				fragment = new InfoFragment();
				break;
			case 1:
				fragment = new StuFragment();
				break;
			case 2:
				fragment = new TchFragment();
				break;
			case 3:
				fragment = new KidFragment();
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MAX_TAB_SIZE;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return TABS[position];
		}
		
	}
	
}
