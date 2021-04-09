package com.ee.hive.entities;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class MsgChat {
	private String chatseq;
	private String roomseq;
	private String sendername;
	private String senderid;
	private String senderavata;
	private String getterid;
	private String content;
	private Timestamp createdat;
	private Timestamp updatedat;
	private String day;
	private String time;
	
	
	public String getSenderavata() {
		return senderavata;
	}
	public void setSenderavata(String senderavata) {
		this.senderavata = senderavata;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getChatseq() {
		return chatseq;
	}
	public void setChatseq(String chatseq) {
		this.chatseq = chatseq;
	}
	public String getRoomseq() {
		return roomseq;
	}
	public void setRoomseq(String roomseq) {
		this.roomseq = roomseq;
	}
	public String getSendername() {
		return sendername;
	}
	public void setSendername(String sendername) {
		this.sendername = sendername;
	}
	public String getSenderid() {
		return senderid;
	}
	public void setSenderid(String senderid) {
		this.senderid = senderid;
	}
	public String getGetterid() {
		return getterid;
	}
	public void setGetterid(String getterid) {
		this.getterid = getterid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getCreatedat() {
		return createdat;
	}
	public void setCreatedat(Timestamp createdat) {
		this.createdat = createdat;
	}
	public Timestamp getUpdatedat() {
		return updatedat;
	}
	public void setUpdatedat(Timestamp updatedat) {
		this.updatedat = updatedat;
	}
	
	
}
