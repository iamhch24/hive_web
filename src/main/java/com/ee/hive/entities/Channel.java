package com.ee.hive.entities;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Channel {
	private String chanid;
	private String name;
	private String workid;
	private String description;
	private LocalDate createdat;
	private LocalDate updatedat;
	private String owneremail;
	
	public String getOwneremail() {
		return owneremail;
	}
	public void setOwneremail(String owneremail) {
		this.owneremail = owneremail;
	}
	public String getChanid() {
		return chanid;
	}
	public void setChanid(String chanid) {
		this.chanid = chanid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWorkid() {
		return workid;
	}
	public void setWorkid(String workid) {
		this.workid = workid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
