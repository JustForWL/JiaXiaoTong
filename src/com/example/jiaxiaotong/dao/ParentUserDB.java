package com.example.jiaxiaotong.dao;

import java.util.ArrayList;

import com.example.jiaxiaotong.bean.ParentUserBean;
import com.example.jiaxiaotong.utils.Logger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * DB for UserBean
 * @author Arthur
 *
 */
public class ParentUserDB extends DBConnection{
    private final String USER_TABLE = "ParentUser";
    private final String USER_ACCOUNT = "user_account";
    private final String USER_NAME = "user_name";
    private final String USER_ICONADDR = "user_icon_addr";
    private final String USER_CLASSMATE = "user_classmate";
    
	public ParentUserDB(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createTable() {
		// TODO Auto-generated method stub
		final SQLiteDatabase db = getReadableDatabase();
		String sql = "CREATE TABLE IF NOT EXISTS " + USER_TABLE + " (" + 
					USER_ACCOUNT + " text primary key not null, " + 
				    USER_NAME + " text not null, " + 
				    USER_ICONADDR + " text not null, " + 
				    USER_CLASSMATE + " text not null" + 
				    ");";
		db.execSQL(sql);
		//super.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		super.onUpgrade(db, oldVersion, newVersion);
	}
    
	public boolean saveUsers(ArrayList<ParentUserBean> users) {
		final SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try{
			SQLiteStatement stmt = null;
			String sql = "insert into " + USER_TABLE + " values (?, ?, ?, ?);";
			stmt = db.compileStatement(sql);
			for(ParentUserBean user : users) {
				stmt.bindString(1, user.getUserAccount());
				stmt.bindString(2, user.getUserName());
				stmt.bindString(3, user.getIconAddr());
				stmt.bindString(4, user.getClassmate());
				stmt.executeInsert();
			}
			db.setTransactionSuccessful();
		} 
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			db.endTransaction();
		}
		return true;
	}
	
	public boolean saveUser(ParentUserBean user) {
		final SQLiteDatabase db = getWritableDatabase();
		try{
		SQLiteStatement stmt = null;
		String sql = "insert into " + USER_TABLE + " values (?, ?, ?, ?);";
		stmt = db.compileStatement(sql);
		stmt.bindAllArgsAsStrings(new String[]{user.getUserAccount(), user.getUserName(), 
								user.getIconAddr(), user.getClassmate()});
		stmt.executeInsert();
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<ParentUserBean> getUsersByClassmate(String classmate) {
		ArrayList<ParentUserBean> users = new ArrayList<ParentUserBean>();
		try{	
			final SQLiteDatabase db = getReadableDatabase();
			Cursor cursor;
			cursor = db.query(USER_TABLE, new String[]{USER_ACCOUNT, USER_NAME, USER_ICONADDR}, 
					USER_CLASSMATE + "='" + classmate + "'", 
					null, null, null, USER_ACCOUNT, null);
			if(cursor.moveToFirst()) {
				for(int i = 0; i < cursor.getCount(); i++) {
					ParentUserBean user = new ParentUserBean();
					user.setUserAccount(cursor.getString(cursor.getColumnIndex(USER_ACCOUNT)));
					user.setUserName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
					user.setIconAddr(cursor.getString(cursor.getColumnIndex(USER_ICONADDR)));
					user.setClassmate(classmate);
					users.add(user);
					cursor.moveToNext();
				}
			}
			cursor.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return users;
	}
	
}
