package com.ee.hive.entities;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Document {
	private String docuseq;
	private String workid;
	private String sendername;
	private String senderid;
	private String line01;
	private String line02;
	private String line03;
	private String cc01;
	private String cc02;
	private String cc03;
	private String cc04;
	private String cc05;
	private String linedepth;
	private String state;
	private String title;
	private String content;
	private String filepath;
	private String mode;
	private String cmd;
	private String day;
	private String time;
	private Timestamp createdat;
	private Timestamp updatedat;
	public String getDocuseq() {
		return docuseq;
	}
	public void setDocuseq(String docuseq) {
		this.docuseq = docuseq;
	}
	public String getWorkid() {
		return workid;
	}
	public void setWorkid(String workid) {
		this.workid = workid;
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
	public String getLine01() {
		return line01;
	}
	public void setLine01(String line01) {
		this.line01 = line01;
	}
	public String getLine02() {
		return line02;
	}
	public void setLine02(String line02) {
		this.line02 = line02;
	}
	public String getLine03() {
		return line03;
	}
	public void setLine03(String line03) {
		this.line03 = line03;
	}
	public String getCc01() {
		return cc01;
	}
	public void setCc01(String cc01) {
		this.cc01 = cc01;
	}
	public String getCc02() {
		return cc02;
	}
	public void setCc02(String cc02) {
		this.cc02 = cc02;
	}
	public String getCc03() {
		return cc03;
	}
	public void setCc03(String cc03) {
		this.cc03 = cc03;
	}
	public String getCc04() {
		return cc04;
	}
	public void setCc04(String cc04) {
		this.cc04 = cc04;
	}
	public String getCc05() {
		return cc05;
	}
	public void setCc05(String cc05) {
		this.cc05 = cc05;
	}
	public String getLinedepth() {
		return linedepth;
	}
	public void setLinedepth(String linedepth) {
		this.linedepth = linedepth;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
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
