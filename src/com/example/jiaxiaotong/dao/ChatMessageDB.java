package com.example.jiaxiaotong.dao;

import java.util.ArrayList;
import java.util.Date;

import com.example.jiaxiaotong.bean.ChatMessageBean;
import com.example.jiaxiaotong.constants.App;
import com.example.jiaxiaotong.utils.Logger;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * DB for ChatMessageBean
 * @author XWLiu
 *
 */
public class ChatMessageDB extends DBConnection {
	private final String MESSAGE_TABLE = "Message";
	private final String MESSAGE_ID = "id"; 
	private final String MESSAGE_FROM = "from_name";
	private final String MESSAGE_TO = "to_name";
	private final String MESSAGE_CONTENT = "content";
	private final String MESSAGE_ISREAD = "read";
	private final String MESSAGE_ISMULTICAST = "multicast";
	private final String MESSAGE_FROM_GROUP = "from_group";
	private final String MESSAGE_DATETIME = "date";
	private final String MESSAGE_FROM_ACCOUNT = "from_account";
	private final String MESSAGE_TO_ACCOUNT = "to_acconut";
	
	public ChatMessageDB(Context context) {
		super(context);
	}

	@Override
	public void createTable() {
		// TODO Auto-generated method stub
		final SQLiteDatabase db = getWritableDatabase();
		String sql = "create table if not exists " + MESSAGE_TABLE + " (" + 
					MESSAGE_ID + " integer primary key autoincrement, " + 
				    MESSAGE_FROM + " text not null, " +
					MESSAGE_TO + " text not null, " + 
				    MESSAGE_CONTENT + " text not null, " +
					MESSAGE_ISREAD + " integer not null, " +
				    MESSAGE_ISMULTICAST + " integer not null, " +
					MESSAGE_FROM_GROUP + " text, " +
					MESSAGE_DATETIME + " integer not null, " + 
					MESSAGE_FROM_ACCOUNT + " text not null, " +
					MESSAGE_TO_ACCOUNT + " text not null" + 
					");";
		db.execSQL(sql);
		super.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		super.onUpgrade(db, oldVersion, newVersion);
	}
	
	public boolean saveChatMessages(ArrayList<ChatMessageBean> chatMessages) {
		final SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try {
			//SQLiteStatement stmt = null;
			String sql = "insert into " + MESSAGE_TABLE + " values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			for(ChatMessageBean chatMessage: chatMessages) {
				db.execSQL(sql, new Object[]{chatMessage.getFrom(), 
											chatMessage.getTo(),
											chatMessage.getContent(),
											chatMessage.getIsRead(),
											chatMessage.getIsMulticast(),
											chatMessage.getFromGroup(),
											chatMessage.getDate().getTime(),
											chatMessage.getFromAccount(),
											chatMessage.getToAccount()
											});
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
	
	public boolean saveChatMessage(ChatMessageBean chatMessage) {
		final SQLiteDatabase db = getWritableDatabase();
		String sql = "insert into " + MESSAGE_TABLE + " values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		try{
			db.execSQL(sql, new Object[]{
					chatMessage.getFrom(), 
					chatMessage.getTo(),
					chatMessage.getContent(),
					chatMessage.getIsRead(),
					chatMessage.getIsMulticast(),
					chatMessage.getFromGroup(),
					chatMessage.getDate().getTime(),
					chatMessage.getFromAccount(),
					chatMessage.getToAccount()
					});
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public ArrayList<ChatMessageBean> getChatMessages() {
		ArrayList<ChatMessageBean> chatMessages = new ArrayList<ChatMessageBean>();
		try{
			final SQLiteDatabase db = getReadableDatabase();
			Cursor cursor;
			cursor = db.query(MESSAGE_TABLE, new String[]{MESSAGE_FROM, MESSAGE_TO,
							MESSAGE_CONTENT, MESSAGE_ISREAD, MESSAGE_ISMULTICAST, 
							MESSAGE_DATETIME, MESSAGE_FROM_ACCOUNT, MESSAGE_TO_ACCOUNT}, 
							null, null, null, null, MESSAGE_DATETIME, null);
			cursor.moveToFirst();
			for(int i = 0; i < cursor.getCount(); i++) {
				ChatMessageBean chatMessage = new ChatMessageBean();
				chatMessage.setFrom(cursor.getString(0));
				chatMessage.setTo(cursor.getString(1));
				chatMessage.setContent(cursor.getString(2));
				chatMessage.setIsRead(cursor.getInt(3));
				chatMessage.setIsMulticast(cursor.getInt(4));
				chatMessage.setDate(new Date(cursor.getLong(5)));
				chatMessage.setFromAccount(cursor.getString(6));
				chatMessage.setToAccount(cursor.getString(7));
				chatMessages.add(chatMessage);
				cursor.moveToNext();
			}
			cursor.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return chatMessages;
	}
	
	public int getUnreadChatMessageCount() {
		int count = -1;
		try{
			final SQLiteDatabase db = getReadableDatabase();
			Cursor cursor;
			cursor = db.query(MESSAGE_TABLE, new String[]{"count(*)"}, "read="+App.IS_READ_NO, 
					null, null, null, null, null);
			cursor.moveToFirst();
			count = cursor.getInt(0);
			cursor.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public ArrayList<ChatMessageBean> getChatMessagesBySource(String source, String name) {
		ArrayList<ChatMessageBean> chatMessages = new ArrayList<ChatMessageBean>();
		try{
			final SQLiteDatabase db = getReadableDatabase();
			Cursor cursor;
			cursor = db.query(MESSAGE_TABLE, new String[]{MESSAGE_FROM, MESSAGE_TO,
							MESSAGE_CONTENT, MESSAGE_ISREAD, MESSAGE_ISMULTICAST, MESSAGE_DATETIME, 
							MESSAGE_FROM_ACCOUNT, MESSAGE_TO_ACCOUNT}, 
							MESSAGE_FROM_ACCOUNT + "='" + source + "' or " + MESSAGE_TO_ACCOUNT + 
							" = '" + source + "' and " + MESSAGE_FROM + "='" + name + "' or " + 
							MESSAGE_TO + "='" + name + "'", 
							null, null, null, MESSAGE_DATETIME, null);
			if(cursor.moveToFirst()){
				for(int i = 0; i < cursor.getCount(); i++) {
					ChatMessageBean chatMessage = new ChatMessageBean();
					chatMessage.setFrom(cursor.getString(0));
					chatMessage.setTo(cursor.getString(1));
					chatMessage.setContent(cursor.getString(2));
					chatMessage.setIsRead(cursor.getInt(3));
					chatMessage.setIsMulticast(cursor.getInt(4));
					chatMessage.setDate(new Date(cursor.getLong(5)));
					chatMessage.setFromAccount(cursor.getString(6));
					chatMessage.setToAccount(cursor.getString(7));
					chatMessages.add(chatMessage);
					cursor.moveToNext();
				}
			}
			cursor.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return chatMessages;
	}
	
	public ArrayList<ChatMessageBean> getChatMessagesByGroup(String group) {
		ArrayList<ChatMessageBean> chatMessages = new ArrayList<ChatMessageBean>();
		try{
			final SQLiteDatabase db = getReadableDatabase();
			Cursor cursor;
			cursor = db.query(MESSAGE_TABLE, new String[]{MESSAGE_FROM, MESSAGE_TO,
							MESSAGE_CONTENT, MESSAGE_ISREAD, MESSAGE_ISMULTICAST, MESSAGE_DATETIME,
							MESSAGE_FROM_ACCOUNT, MESSAGE_TO_ACCOUNT}, 
							MESSAGE_FROM_GROUP + "='" + group + "'", 
							null, null, null, MESSAGE_DATETIME);
			cursor.moveToFirst();
			for(int i = 0; i < cursor.getCount(); i++) {
				ChatMessageBean chatMessage = new ChatMessageBean();
				chatMessage.setFrom(cursor.getString(0));
				chatMessage.setTo(cursor.getString(1));
				chatMessage.setContent(cursor.getString(2));
				chatMessage.setIsRead(cursor.getInt(3));
				chatMessage.setIsMulticast(cursor.getInt(4));
				chatMessage.setDate(new Date(cursor.getLong(5)));
				chatMessage.setFromAccount(cursor.getString(6));
				chatMessage.setToAccount(cursor.getString(7));
				chatMessages.add(chatMessage);
				cursor.moveToNext();
			}
			cursor.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return chatMessages;
	}
	
	public boolean updateISREAD(ChatMessageBean message) {
		try{
			final SQLiteDatabase db = getReadableDatabase();
			String sql = "UPDATE " + MESSAGE_TABLE + " SET " +
						 MESSAGE_ISREAD + "=" + App.IS_READ_YES + 
						 " WHERE " + 
						MESSAGE_FROM +  "='" + message.getFrom() + "'" +
						" AND " + 
						MESSAGE_FROM_ACCOUNT + "='" + message.getFromAccount() + "'" + 
						" AND " + 
						MESSAGE_TO + "='" + message.getTo() + "'" + 
						" AND " + 
						MESSAGE_TO_ACCOUNT + "='"+ message.getToAccount() + "'" + 
						" AND " +
						MESSAGE_DATETIME + "=" + message.getDate().getTime() + ";";
			db.execSQL(sql);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public int getMessageNumByDate(Date date) {
		try{
			final SQLiteDatabase db = getReadableDatabase();
			String sql = "SELECT * FROM " + MESSAGE_TABLE + " WHERE " + MESSAGE_DATETIME + 
						"=" + date.getTime() + ";";
			Cursor cursor = db.rawQuery(sql, null);
			return cursor.getCount();
		} catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
