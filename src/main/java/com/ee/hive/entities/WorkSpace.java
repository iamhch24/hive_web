package com.ee.hive.entities;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class WorkSpace {
	private String workid;
	private String name;
	private String ownerid;
	private String confirmcode;
	private LocalDate codeexpiredat;
	private LocalDate createdat;
	private LocalDate updatedat;
	private String title;
	private String owneremail;
	
	
	public String getOwneremail() {
		return owneremail;
	}
	public void setOwneremail(String owneremail) {
		this.owneremail = owneremail;
	}
	public String getWorkid() {
		return workid;
	}
	public void setWorkid(String workid) {
		this.workid = workid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwnerid() {
		return ownerid;
	}
	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
	}
	public String getConfirmcode() {
		return confirmcode;
	}
	public void setConfirmcode(String confirmcode) {
		this.confirmcode = confirmcode;
	}
	public LocalDate getCodeexpiredat() {
		return codeexpiredat;
	}
	public void setCodeexpiredat(LocalDate codeexpiredat) {
		this.codeexpiredat = codeexpiredat;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
