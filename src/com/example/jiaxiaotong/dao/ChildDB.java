package com.example.jiaxiaotong.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.jiaxiaotong.bean.ChildBean;
import com.example.jiaxiaotong.utils.Logger;
import com.example.jiaxiaotong.utils.SharePreferencesUtil;

import java.util.ArrayList;

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
					LOG + " TEXT" +
					");";
		db.execSQL(sql);
	}
	
	public void save(String name, String state, String log) {
		final SQLiteDatabase db = getWritableDatabase();
		String sql = "INSERT INTO " + CHILD_TABLE + " VALUES(NULL, ?, ?, ?)";
		db.execSQL(sql, new String[]{name, state, log});
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
	
	public ArrayList<String> getLogsByState(String childName, String state) {
		ArrayList<String> logs = new ArrayList<String>();
		try{
			final SQLiteDatabase db = getReadableDatabase();
			String sql = "SELECT " + LOG + " FROM " + CHILD_TABLE + " WHERE " + 
						NAME + "='" + childName + "' AND " + STATE + "='" + 
						state + "';";
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor.moveToFirst()){
				logs.add(cursor.getString(0));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return logs;
	}
}
