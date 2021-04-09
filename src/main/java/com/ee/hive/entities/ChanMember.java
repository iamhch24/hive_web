package com.ee.hive.entities;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ChanMember {
	private int seq;
	private String chanid;
	private String userid;
	private LocalDate createdat;
	private LocalDate updatedat;
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getChanid() {
		return chanid;
	}
	public void setChanid(String chanid) {
		this.chanid = chanid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
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
