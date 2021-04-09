package com.ee.hive.entities;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Avatas {
	private String adress;

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}
}
