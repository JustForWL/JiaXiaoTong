package com.example.jiaxiaotong.bean;

import java.util.Date;

/**
 * Chat message bean
 * 字段from表示消息的来源，可以是单个user，也可以是群的名字
 * 字段to表示消息的去向，可以是单个user，也可以是群的名字
 * 字段data表示消息的timestamp
 * 字段content表示消息的内容
 * 字段isRead表示是否读过
 * 字段isMulticast表示是否为群消息
 * @author Arthur
 *
 */
public class ChatMessageBean {

	private String from = null;
	private String fromAccount = null;
	private String to = null;
	private String toAccount = null;
	private Date date = null;
	private String content = null;
	private int isRead;
	private int isMulticast;
	private String from_group = null;
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public int getIsMulticast() {
		return isMulticast;
	}
	public void setIsMulticast(int isMulticast) {
		this.isMulticast = isMulticast;
	}
	public String getFrom_group() {
		return from_group;
	}
	public void setFrom_group(String from_group) {
		this.from_group = from_group;
	}
	public String getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}
	public String getToAccount() {
		return toAccount;
	}
	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}
	
	
}
