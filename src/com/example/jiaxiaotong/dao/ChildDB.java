package com.example.jiaxiaotong.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.jiaxiaotong.bean.ChildBean;
import com.example.jiaxiaotong.utils.Logger;
import com.example.jiaxiaotong.utils.SharePreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * DB for Child
 * @author Arthur
 *
 */
public class ChildDB extends DBConnection{
	private final String CHILD_TABLE = "Child";
	private final String ID = "id";
	private final String NAME = "name";
	private final String STATE = "state";
	private final String LOG = "log";
	private final String CLASSNAME = "class_name";
	
	public ChildDB(Context context) {
		super(context);
	}
	
	@Override
	public void createTable() {
		final SQLiteDatabase db = getWritableDatabase();
		String sql = "CREATE TABLE IF NOT EXISTS " + CHILD_TABLE + "(" + 
					ID + " INTEGER PRIMARY KEY," + 
					NAME + " TEXT NOT NULL," +
					STATE + " TEXT NOT NULL," + 
					LOG + " TEXT, " + 
					CLASSNAME + " TEXT NOT NULL" +
					");";
		db.execSQL(sql);
	}
	
	public void save(String name, String state, String log, String className) {
		final SQLiteDatabase db = getWritableDatabase();
		String sql = "INSERT INTO " + CHILD_TABLE + " VALUES(NULL, ?, ?, ?, ?)";
		db.execSQL(sql, new String[]{name, state, log, className});
		try{
		} catch(Exception e) {
			e.printStackTrace();
		}
		db.close();
	}
	
	public ArrayList<ChildBean> getChildren() {
		ArrayList<ChildBean> children = new ArrayList<ChildBean>();
		try{
			final SQLiteDatabase db = getReadableDatabase();
			String sql = "SELECT DISTINCT " + NAME + " FROM " + CHILD_TABLE;
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor.moveToFirst()) {
				for(int i = 0; i < cursor.getCount(); i++) {
					ChildBean child = new ChildBean();
					child.setChildName(cursor.getString(0));
					child.setChildIcon(cursor.getString(0) + ".png");
					children.add(child);
					cursor.moveToNext();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return children;
	}
	
	public ArrayList<String> getClassNames() {
		ArrayList<String> classNames = new ArrayList<String>();
		try{
			final SQLiteDatabase db = getReadableDatabase();
			String sql = "SELECT DISTINCT " + CLASSNAME + " FROM " + CHILD_TABLE;
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor.moveToFirst()) {
				for(int i = 0; i < cursor.getCount(); i++) {
					classNames.add(cursor.getString(0));
					cursor.moveToNext();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return classNames;
	}
	
	public ArrayList<Map<String, String>> getLogsByState(String childName, String state) {
		ArrayList<Map<String, String>> logs = new ArrayList<Map<String, String>>();
		try{
			final SQLiteDatabase db = getReadableDatabase();
			String sql = "SELECT " + LOG + " FROM " + CHILD_TABLE + " WHERE " + 
						NAME + "='" + childName + "' AND " + STATE + "='" + 
						state + "';";
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor.moveToFirst()){
				for(int i = 0; i < cursor.getCount(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put(childName, cursor.getString(0));
					logs.add(map);
					cursor.moveToNext();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return logs;
	}
	
	public String getClassName(String childName) {
		String className = null;
		final SQLiteDatabase db = getReadableDatabase();
		try{
			String sql = "SELECT " + CLASSNAME + " FROM " + CHILD_TABLE 
						+ " WHERE " + NAME + "='" + childName + "' LIMIT 1;";
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor.moveToFirst()) {
				className = cursor.getString(0);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return className;
	}
	
	public String getChildName(String className) {
		String childName = null;
		final SQLiteDatabase db = getReadableDatabase();
		try{
			String sql = "SELECT " + NAME + " FROM " + CHILD_TABLE 
						+ " WHERE " + CLASSNAME + "='" + className + "' LIMIT 1;";
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor.moveToFirst()) {
				className = cursor.getString(0);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return childName;
	}
}
