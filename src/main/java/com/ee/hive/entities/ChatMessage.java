package com.ee.hive.entities;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ChatMessage {

	private MessageType type;
	private String content;
	private String sender;
	private String userid;
	private String otherid;
	private String chanid;
	private String roomseq;

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOtherid() {
		return otherid;
	}

	public void setOtherid(String otherid) {
		this.otherid = otherid;
	}

	public String getChanid() {
		return chanid;
	}

	public void setChanid(String chanid) {
		this.chanid = chanid;
	}

	public String getRoomseq() {
		return roomseq;
	}

	public void setRoomseq(String roomseq) {
		this.roomseq = roomseq;
	}

}