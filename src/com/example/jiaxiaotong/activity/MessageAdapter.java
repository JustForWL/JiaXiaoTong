package com.example.jiaxiaotong.activity;

import java.util.ArrayList;

import com.example.jiaxiaotong.R;
import com.example.jiaxiaotong.bean.ChatMessageBean;
import com.example.jiaxiaotong.utils.SharePreferencesUtil;
import com.example.jiaxiaotong.utils.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MessageAdapter extends BaseAdapter {
	    private ArrayList<ChatMessageBean> messages = null;
		private LayoutInflater layoutInflater = null;
		private Context context = null;
		private int COME = 1; //发送的消息
		private int TO = 0; //接收的消息
		
		public MessageAdapter(Context context, ArrayList<ChatMessageBean> messages) {
			this.messages = messages;
			this.layoutInflater = LayoutInflater.from(context);
			this.context = context;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.messages.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return this.messages.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			ChatMessageBean message = this.messages.get(position);
			if(message.getFromAccount().equals(SharePreferencesUtil.readLoginAccount(context))){
				return TO;
			}else {
				return COME;
			}
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ChatMessageBean message = messages.get(position);
			ViewHolder viewHolder = null;
			if(null == convertView) {
				if(TO == getItemViewType(position)){ //当前登录账号发送的消息显示在右方
					convertView = this.layoutInflater.inflate(
							R.layout.chatting_item_msg_text_right, parent, false);
					viewHolder = new ViewHolder();
					viewHolder.sendTime = (TextView) convertView.findViewById(R.id.tv_sendtime_to);
					viewHolder.userHead = (ImageView) convertView.findViewById(R.id.iv_userhead_to);
					viewHolder.content = (TextView) convertView.findViewById(R.id.tv_chatcontent_to);
					viewHolder.userName = (TextView) convertView.findViewById(R.id.tv_username_to);
				}else { //发送给当前登录账号的消息显示在左方
					convertView = this.layoutInflater.inflate(
							R.layout.chatting_item_msg_text_left, null);
					viewHolder = new ViewHolder();
					viewHolder.sendTime = (TextView) convertView.findViewById(R.id.tv_sendtime_from);
					viewHolder.userHead = (ImageView) convertView.findViewById(R.id.iv_userhead_from);
					viewHolder.content = (TextView) convertView.findViewById(R.id.tv_chatcontent_from);
					viewHolder.userName = (TextView) convertView.findViewById(R.id.tv_username_from);
				}
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.sendTime.setText(Util.formatDate(message.getDate()));
			viewHolder.content.setText(message.getContent());
			viewHolder.userName.setText(message.getFrom());
			viewHolder.userHead.setBackground(Util.getBackground(message.getFrom()+".png"));
			return convertView;
		}
		
	}
	
	class ViewHolder {
		public TextView sendTime;
		public ImageView userHead;
		public TextView content;
		public TextView userName;
	}

