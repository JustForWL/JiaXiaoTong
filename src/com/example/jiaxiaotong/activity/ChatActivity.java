package com.example.jiaxiaotong.activity;
import java.util.ArrayList;
import java.util.Date;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.DelayInformation;
import com.example.jiaxiaotong.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract.Data;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.jiaxiaotong.bean.ChatMessageBean;
import com.example.jiaxiaotong.constants.App;
import com.example.jiaxiaotong.dao.ChatMessageDB;
import com.example.jiaxiaotong.dao.ParentUserDB;
import com.example.jiaxiaotong.service.ReceiverService;
import com.example.jiaxiaotong.service.ReceiverService.OnNewMessage;
import com.example.jiaxiaotong.utils.SharePreferencesUtil;
import com.example.jiaxiaotong.utils.T;
import com.example.jiaxiaotong.utils.Util;
import com.example.jiaxiaotong.utils.XMPPManager;
/**
 * 聊天界面
 * @author Arthur
 *
 */
public class ChatActivity extends Activity implements OnNewMessage{
	private TextView talkTo = null;
	private static final String TALK = "talkTo";
	private static final String ACCOUNT = "account";
	private String talk = null;
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
		talkTo = (TextView) findViewById(R.id.talkTo);
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			talk = bundle.getString(TALK);
		    account = bundle.getString(ACCOUNT);
			talkTo.setText(talk);
		}
		messages = chatMessageDB.getChatMessagesBySource(account, talk);
		for(ChatMessageBean messge : messages) {
			chatMessageDB.updateISREAD(messge);
		}
		messageAdapter = new MessageAdapter(ChatActivity.this, messages);
		messageView.setAdapter(messageAdapter);
		sendBtn = (Button) findViewById(R.id.btn_send);
		editText = (EditText) findViewById(R.id.et_sendmessage);
		sendBtn.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String messageText = editText.getText().toString();
				if(messageText.equals(""))
					T.showShort(ChatActivity.this, "请输入内容");
				else {
					ChatMessageBean message = new ChatMessageBean();
					message.setContent(editText.getText().toString());
					message.setDate(new Date());
					message.setFromAccount(SharePreferencesUtil.readLoginAccount(ChatActivity.this));
					message.setFrom(SharePreferencesUtil.readCurrentChild(ChatActivity.this));
					message.setToAccount(account);
					message.setTo(talk);
					message.setIsMulticast(App.IS_MULTICAST_NO);
					message.setIsRead(App.IS_READ_YES);
					chatMessageDB.saveChatMessage(message);
					messages.add(message);
					messageAdapter.notifyDataSetChanged();
					messageView.setSelection(messages.size());
					try {
						XMPPConnection connection = XMPPManager.getInstance().getConnection();
						ChatManager chatManager = connection.getChatManager();
						//Chat chat = chatManager.createChat(account, null);
						Chat chat = chatManager.createChat("t111"+App.SERVER_DOMAIN, null);
						chat.sendMessage(SharePreferencesUtil.readCurrentChild(ChatActivity.this) + "@"+ messageText);
						editText.setText("");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						T.showShort(ChatActivity.this, "请重新发送");
						e.printStackTrace();
					}
					
				}
			}
		});
		messageView.setSelection(messages.size() - 1);
	}

	@Override
	public void newMessage(ChatMessageBean message, String userName, Message rawMessage) {
		// TODO Auto-generated method stub
		String fromAccount = message.getFromAccount();
		if(account.equals(fromAccount)) {
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
		} else {
			Util.showNotify(this, rawMessage, userName, 0, false, null);
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
