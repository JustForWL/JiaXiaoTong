package com.example.jiaxiaotong.dao;

import java.util.ArrayList;

import com.example.jiaxiaotong.bean.TeacherUserBean;
import com.example.jiaxiaotong.utils.Logger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class TeacherUserDB extends DBConnection {
	private final String TEACHER_TABLE = "TeacherUser";
	private final String USER_ACCOUNT = "user_account";
	private final String USER_NAME = "user_name";
	private final String USER_ICONADDR = "user_icon_addr";
	private final String USER_ROLE = "user_role";
	private final String STUDENT = "student";
	
	public TeacherUserDB(Context context) {
		super(context);
	}
	
	@Override
	public void createTable() {
		// TODO Auto-generated method stub
		final SQLiteDatabase db = getWritableDatabase();
		String sql = "CREATE TABLE IF NOT EXISTS " + TEACHER_TABLE + " (" + 
					USER_ACCOUNT + " TEXT PRIMARY KEY, " + 
					USER_NAME + " TEXT NOT NULL, " + 
					USER_ICONADDR + " TEXT NOT NULL, " + 
					USER_ROLE + " TEXT NOT NULL, " + 
					STUDENT + " TEXT NOT NULL" + 
					");";
		db.execSQL(sql);
		//super.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		super.onUpgrade(db, oldVersion, newVersion);
	}

	public boolean saveUsers(ArrayList<TeacherUserBean> users) {
		final SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try{
			SQLiteStatement stmt = null;
			String sql = "insert into " + TEACHER_TABLE + " values (?, ?, ?, ?, ?);";
			stmt = db.compileStatement(sql);
			for(TeacherUserBean user : users) {
				stmt.bindString(1, user.getUserAccount());
				stmt.bindString(2, user.getUserName());
				stmt.bindString(3, user.getIconAddr());
				stmt.bindString(4, user.getRole());
				stmt.bindString(5, user.getStudent());
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
	
	public boolean saveUser(TeacherUserBean user) {
		final SQLiteDatabase db = getWritableDatabase();
		try{
			SQLiteStatement stmt = null;
			String sql = "insert into " + TEACHER_TABLE + " values (?, ?, ?, ?, ?);";
			stmt = db.compileStatement(sql);
			stmt.bindAllArgsAsStrings(new String[]{user.getUserAccount(), user.getUserName(), 
									user.getIconAddr(), user.getRole(), user.getStudent()});
			stmt.executeInsert();
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<TeacherUserBean> getUsersByStudent(String student) {
		ArrayList<TeacherUserBean> users = new ArrayList<TeacherUserBean>();
		try{
			final SQLiteDatabase db = getReadableDatabase();
			Cursor cursor;
			cursor = db.query(TEACHER_TABLE, new String[]{USER_ACCOUNT, USER_NAME, USER_ICONADDR, USER_ROLE}, 
					STUDENT + "='" + student + "'", 
					null, null, null, USER_ROLE, null);
			if(cursor.moveToFirst()) {
				for(int i = 0; i < cursor.getCount(); i++) {
					TeacherUserBean user = new TeacherUserBean();
					user.setUserAccount(cursor.getString(cursor.getColumnIndex(USER_ACCOUNT)));
					user.setUserName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
					user.setIconAddr(cursor.getString(cursor.getColumnIndex(USER_ICONADDR)));
					user.setRole(cursor.getString(cursor.getColumnIndex(USER_ROLE)));
					user.setStudent(student);
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
