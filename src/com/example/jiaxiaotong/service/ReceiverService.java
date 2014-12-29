package com.example.jiaxiaotong.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.DelayInformation;

import com.example.jiaxiaotong.activity.ParentFrame;
import com.example.jiaxiaotong.bean.ChatMessageBean;
import com.example.jiaxiaotong.bean.ParentUserBean;
import com.example.jiaxiaotong.constants.App;
import com.example.jiaxiaotong.dao.ChatMessageDB;
import com.example.jiaxiaotong.dao.ChildDB;
import com.example.jiaxiaotong.dao.ParentUserDB;
import com.example.jiaxiaotong.utils.SharePreferencesUtil;
import com.example.jiaxiaotong.utils.Util;
import com.example.jiaxiaotong.utils.XMPPManager;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Pair;

public class ReceiverService extends Service {
	private XMPPConnection connection = null;
	public static ArrayList<OnNewMessage> messageListeners = null;
	private ChatMessageDB cdb = null;
	private ChatMessageBean chatMessage = null;
	private ParentUserBean user = null;
	private ParentUserDB pdb = null;
	private ChildDB childDB = null;
	private static Map<String, MultiUserChat> multiUserChat = null;
	 
	public interface OnNewMessage {
		public void newMessage(ChatMessageBean message, String userName, Message rawMessage);
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new ReceiverServiceBinder();
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		try {
			pdb = new ParentUserDB(this);
			cdb = new ChatMessageDB(this);
			childDB = new ChildDB(this);
			connection = XMPPManager.getInstance().getConnection();
			messageListeners = new ArrayList<OnNewMessage>();
			multiUserChat = new HashMap<String, MultiUserChat>();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		getOfflineMessage();
		ChatManager chatManager = connection.getChatManager();
		chatManager.addChatListener(new MyChatManagerListener());
		joinMultiChat();
	}
	
	protected void getOfflineMessage() {
		OfflineMessageManager offlineManager = new OfflineMessageManager(connection);
		Map<String, ArrayList<Message>> offlineMsgs = 
				new HashMap<String, ArrayList<Message>>();
		try{
			Iterator<Message> itor = offlineManager.getMessages();
			
			while(itor.hasNext()) {
				Message message = itor.next();
				saveNewMessage(message);
				String fromUser = message.getFrom().split("@")[0];
				String fromName = message.getBody().split("@", 2)[0];
				String combine = fromUser + "/" + fromName;
				if(offlineMsgs.containsKey(combine)) {
					offlineMsgs.get(combine).add(message);
				}else {
					ArrayList<Message> temp = new ArrayList<Message>();
					temp.add(message);
					offlineMsgs.put(combine, temp);
				}
			}
			Set<String> keys = offlineMsgs.keySet();
			Iterator<String> offIt = keys.iterator();
			while(offIt.hasNext()) {
				String key = offIt.next();
				String fromName = key.split("/")[1];
				ArrayList<Message> mgs = offlineMsgs.get(key);
				if(mgs.size() > 0) {
					for(Message mg : mgs) {
						saveNewMessage(mg);
					}
					//get last message
					Message mg = mgs.get(mgs.size() - 1);
					Util.showNotify(this, mg, fromName, mgs.size(), false, null);
				}
			}
			offlineManager.deleteMessages();
			Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String, MultiUserChat> getMultiUserChat() {
	    	return multiUserChat;
	}
	
	public void joinMultiChat() {
		ArrayList<String> classNames = childDB.getClassNames();
		for(String className : classNames) {
			MultiUserChat multiChat = new MultiUserChat(connection, 
					className + "@conference." + connection.getServiceName());
			try {
				multiChat.join(SharePreferencesUtil.readLoginAccount(this));
                multiChat.addMessageListener(new PacketListener() {
					
					@Override
					public void processPacket(Packet packet) {
						// TODO Auto-generated method stub
						String from_ = packet.getFrom();
						String fromAccount = from_.substring(from_.indexOf("/") + 1, 
								from_.length());
						if(!fromAccount.equals(SharePreferencesUtil.readLoginAccount(
								ReceiverService.this))) {
							if(!packetSaved(packet)) {
								saveMutliNewMessage(packet);
								if(messageListeners.size() > 0) {
									for(OnNewMessage messageListener : messageListeners) {
									//messageListener.newMessage(chatMessage, user.getUserName(), message);
									messageListener.newMessage(chatMessage, "bb", (Message) packet);
									}
								}else {
									String from = packet.getFrom();
									String toName = from.substring(0, from.indexOf("@"));
									Util.showNotify(ReceiverService.this, (Message) packet, "bb", 0, true, toName);
									//Util.showNotify(ReceiverService.this, message, user.getUserName(), 0, true, toName);
								}
							}
						}
					}
				});
				multiUserChat.put(className, multiChat);
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void saveMutliNewMessage(Packet packet) {
		chatMessage = new ChatMessageBean();
		String from = packet.getFrom();
		String fromAccount = from.substring(from.indexOf("/") + 1, from.length());
		Message message = (Message) packet;
		String fromName = message.getBody().split("@", 2)[0];
		String content = message.getBody().split("@", 2)[1];
		user = pdb.getUserByNameAndAccount(fromName, fromAccount);
		DelayInformation delay = (DelayInformation) message.getExtension("x", "jabber:x:delay");
		Date sendDate = null;
		if(delay != null){
			sendDate = delay.getStamp();
		}else {
			sendDate = new Date();
		}
		chatMessage.setContent(content);
		chatMessage.setDate(sendDate);
		chatMessage.setFrom("bb");
		//chatMessage.setFrom(user.getUserName());
		chatMessage.setFromAccount(fromAccount);
		chatMessage.setIsMulticast(App.IS_MULTICAST_YES);
		chatMessage.setIsRead(App.IS_READ_NO);
		String toName = from.substring(0, from.indexOf("@"));
		String toAccount = from.substring(0, from.indexOf("/"));
		chatMessage.setToAccount(toAccount);
		chatMessage.setTo(toName);
		cdb.saveChatMessage(chatMessage);
		
	}
	
	protected void saveNewMessage(Message message) {
		chatMessage = new ChatMessageBean();
		String fromAccount = message.getFrom();
		fromAccount = fromAccount.substring(0, fromAccount.indexOf("@"));
		String fromName = message.getBody().split("@", 2)[0];
		String content = message.getBody().split("@", 2)[1];
		user = pdb.getUserByNameAndAccount(fromName, fromAccount);
		DelayInformation delay = (DelayInformation) message.getExtension("x", "jabber:x:delay");
		Date sendDate = null;
		if(delay != null){
			sendDate = delay.getStamp();
		}else {
			sendDate = new Date();
		}
		chatMessage.setContent(content);
		chatMessage.setDate(sendDate);
		chatMessage.setFrom("bb");
		//chatMessage.setFrom(user.getUserName());
		chatMessage.setFromAccount(fromAccount);
		chatMessage.setIsMulticast(App.IS_MULTICAST_NO);
		chatMessage.setIsRead(App.IS_READ_NO);
		chatMessage.setToAccount(SharePreferencesUtil.readLoginAccount(ReceiverService.this));
		chatMessage.setTo(SharePreferencesUtil.readCurrentChild(ReceiverService.this));
		cdb.saveChatMessage(chatMessage);
	}
	
	protected boolean packetSaved(Packet packet) {
		Message message = (Message) packet;
		DelayInformation delay = (DelayInformation) message.getExtension("x", "jabber:x:delay");
		Date sendDate = null;
		if(delay != null){
			sendDate = delay.getStamp();
		}else {
			sendDate = new Date();
		}
		System.out.println(cdb.getMessageNumByDate(sendDate));
		if(cdb.getMessageNumByDate(sendDate) > 0) {
			return true;
		}else {
			return false;
		}
	}
	class ReceiverServiceBinder extends Binder {
		ReceiverService getService() {
			return ReceiverService.this;
		}
	}
	
	class MyChatManagerListener implements ChatManagerListener {

		@Override
		public void chatCreated(Chat chat, boolean arg1) {
			// TODO Auto-generated method stub
			chat.addMessageListener(new MyMessageListener());
		}
		
	}
	
	class MyMessageListener implements MessageListener {

		@Override
		public void processMessage(Chat chat, Message message) {
			// TODO Auto-generated method stub
			saveNewMessage(message);
			if(messageListeners.size() > 0) {
				for(OnNewMessage messageListener : messageListeners) {
				//messageListener.newMessage(chatMessage, user.getUserName(), message);
				messageListener.newMessage(chatMessage, "bb", message);
				}
			}else {
				//Util.showNotify(ReceiverService.this, message, user.getUserName(), 0, false, null);
				Util.showNotify(ReceiverService.this, message, "bb", 0, false, null);
			}
		}
		
	}
}
