package com.example.jiaxiaotong.dao;

import com.example.jiaxiaotong.constants.App;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Base class for DB
 * @author XWLiu
 *
 */
public class DBConnection extends SQLiteOpenHelper{
   
	public DBConnection(Context context) {
		super(context, App.DATABASE_NAME, null, App.DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		createTable();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void createTable() {
		
	}

}
