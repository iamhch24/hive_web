package com.ee.hive.entities;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Chat {
	String chatid;

	public String getChatid() {
		return chatid;
	}

	public void setChatid(String chatid) {
		this.chatid = chatid;
	}
}
