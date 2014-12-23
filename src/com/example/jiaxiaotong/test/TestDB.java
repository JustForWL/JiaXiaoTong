package com.example.jiaxiaotong.test;

import junit.framework.Assert;

import com.example.jiaxiaotong.activity.ParentFrame;
import com.example.jiaxiaotong.bean.ParentUserBean;
import com.example.jiaxiaotong.dao.ParentUserDB;

import android.app.Activity;
import android.test.AndroidTestCase;

public class TestDB extends AndroidTestCase {
	
	public void testParentUserAdd() throws Exception {
		ParentUserDB parentDB = new TestActivity().getDB();
		ParentUserBean parentBean = new ParentUserBean();
		parentBean.setUserAccount("aa");
		parentBean.setUserName("bb");
		parentBean.setIconAddr("cc");
		parentBean.setClassmate("dd");
		Assert.assertTrue(parentDB.saveUser(parentBean));
		
	}
}

class TestActivity extends Activity{
	public ParentUserDB parentDB = null;
	public ParentUserDB getDB(){
		parentDB = new ParentUserDB(this);
		return parentDB;
	}
}