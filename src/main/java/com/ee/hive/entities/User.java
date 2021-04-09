package com.ee.hive.entities;

import java.security.Timestamp;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class User {
	private String userid;
	private String email;
	private String name;
	private String pwd;
	private String phone;
	private String job;
	private String photo;
	private Timestamp createdat;
	private Timestamp updatedat;
	private String lastwork;
	private String lastchannel;
	private String lastmessage;
	
	public String getLastmessage() {
		return lastmessage;
	}
	public void setLastmessage(String lastmessage) {
		this.lastmessage = lastmessage;
	}
	public String getLastchannel() {
		return lastchannel;
	}
	public void setLastchannel(String lastchannel) {
		this.lastchannel = lastchannel;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
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
	public String getLastwork() {
		return lastwork;
	}
	public void setLastwork(String lastwork) {
		this.lastwork = lastwork;
	}
	
}