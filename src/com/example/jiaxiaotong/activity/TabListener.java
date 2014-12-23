package com.example.jiaxiaotong.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Created by John on 12/15/2014.
 */
public class TabListener<T extends Fragment> implements ActionBar.TabListener {
    private Fragment fragment;
    private final Activity activity;
    private final String mTag;
    private final Class<T> mClass;

    public TabListener(Activity mactivity, String Tagt, Class<T> cls){
        activity = mactivity;
        mTag = Tagt;
        mClass = cls;
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft){
        if(fragment == null){
            fragment = Fragment.instantiate(activity, mClass.getName());
            ft.add(android.R.id.content, fragment, mTag);
        }else {
            ft.attach(fragment);
        }
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft){
        if(fragment !=  null){
            ft.detach(fragment);
        }
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft){

    }
}


