package com.ee.hive.entities;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class WorkMember {
	private int seq;
	private String workid;
	private String userid;
	private String level;
	private LocalDate createdat;
	private LocalDate updatedat;
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getWorkid() {
		return workid;
	}
	public void setWorkid(String workid) {
		this.workid = workid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public LocalDate getCreatedat() {
		return createdat;
	}
	public void setCreatedat(LocalDate createdat) {
		this.createdat = createdat;
	}
	public LocalDate getUpdatedat() {
		return updatedat;
	}
	public void setUpdatedat(LocalDate updatedat) {
		this.updatedat = updatedat;
	}
	
	
}
