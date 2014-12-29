package com.example.jiaxiaotong.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jiaxiaotong.R;
import com.example.jiaxiaotong.bean.ChatMessageBean;
import com.example.jiaxiaotong.constants.App;
import com.example.jiaxiaotong.dao.ChatMessageDB;
import com.example.jiaxiaotong.service.ReceiverService;
import com.example.jiaxiaotong.service.ReceiverService.OnNewMessage;
import com.example.jiaxiaotong.utils.SharePreferencesUtil;
import com.example.jiaxiaotong.utils.T;
import com.example.jiaxiaotong.utils.Util;
import com.example.jiaxiaotong.utils.XMPPManager;

public class MultiChatActivity extends Activity implements OnNewMessage {
	private TextView classNameTv = null;
	private String childName = null;
	private String className = null;
	private String account = null;
	private ListView messageView = null;
	private MessageAdapter messageAdapter = null;
	private ArrayList<ChatMessageBean> messages = null;
	private Button sendBtn = null;
	private EditText editText = null;
	private ChatMessageDB chatMessageDB= null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		initView();
	}
	
	private void initView() {
		chatMessageDB = new ChatMessageDB(this);
		ReceiverService.messageListeners.add(this);
		messageView = (ListView) findViewById(R.id.listview);
		messageView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		classNameTv = (TextView) findViewById(R.id.talkTo);
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			className = bundle.getString(App.CLASSNAME);
		    account = bundle.getString(App.ACCOUNT);
		    childName = bundle.getString(App.CHILDNAME);
			classNameTv.setText(className);
		}
		messages = chatMessageDB.getChatMessagesBySource(account, className);
		for(ChatMessageBean messge : messages) {
			chatMessageDB.updateISREAD(messge);
		}
		messageAdapter = new MessageAdapter(this, messages);
		messageView.setAdapter(messageAdapter);
		sendBtn = (Button) findViewById(R.id.btn_send);
		editText = (EditText) findViewById(R.id.et_sendmessage);
        sendBtn.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String messageText = editText.getText().toString();
				if(messageText.equals(""))
					T.showShort(MultiChatActivity.this, "请输入内容");
				else {
					ChatMessageBean message = new ChatMessageBean();
					message.setContent(editText.getText().toString());
					message.setDate(new Date());
					message.setFromAccount(SharePreferencesUtil.readLoginAccount(MultiChatActivity.this));
					message.setFrom(childName);
					message.setToAccount(account);
					message.setTo(className);
					message.setIsMulticast(App.IS_MULTICAST_YES);
					message.setIsRead(App.IS_READ_YES);
					chatMessageDB.saveChatMessage(message);
					messages.add(message);
					messageAdapter.notifyDataSetChanged();
					messageView.setSelection(messages.size());
					try {
						Map<String, MultiUserChat> mucs = ReceiverService.getMultiUserChat();
						MultiUserChat muc = mucs.get(MultiChatActivity.this.getClassName());
						muc.sendMessage(childName + "@"+ messageText);
						editText.setText("");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						T.showShort(MultiChatActivity.this, "请重新发送");
						e.printStackTrace();
					}
					
				}
			}
		});
		messageView.setSelection(messages.size() - 1);
	}
	
	public String getClassName() {
		return this.className;
	}
	@Override
	public void newMessage(ChatMessageBean message, String userName,
			Message rawMessage) {
		// TODO Auto-generated method stub
		String from = rawMessage.getFrom();
		String fromClassName = rawMessage.getFrom().substring(0, from.indexOf("@"));
		if(fromClassName.equals(className)) {
			chatMessageDB.updateISREAD(message);
			messages.add(message);
			messageView.post(new Runnable() {
	
				@Override
				public void run() {
					// TODO Auto-generated method stub
					messageAdapter.notifyDataSetChanged();
					messageView.setItemChecked(messageAdapter.getCount() - 1, true);
					messageView.setSelection(messageAdapter.getCount() - 1);	
					messageView.smoothScrollToPosition(messages.size() - 1);
				}
				
			});
		}else {
			Util.showNotify(this, rawMessage, userName, 0, true, className);
		}
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		ReceiverService.messageListeners.add(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		ReceiverService.messageListeners.remove(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
