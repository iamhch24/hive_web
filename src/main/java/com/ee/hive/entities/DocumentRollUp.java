package com.ee.hive.entities;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class DocumentRollUp {
	private int cnt_cc;
	private int cnt_d;
	private int cnt_w;
	private int cnt_1;
	private int cnt_2;
	private int cnt_3;
	private int cnt_a;
	private int cnt_h;
	public int getCnt_cc() {
		return cnt_cc;
	}
	public void setCnt_cc(int cnt_cc) {
		this.cnt_cc = cnt_cc;
	}
	public int getCnt_d() {
		return cnt_d;
	}
	public void setCnt_d(int cnt_d) {
		this.cnt_d = cnt_d;
	}
	public int getCnt_w() {
		return cnt_w;
	}
	public void setCnt_w(int cnt_w) {
		this.cnt_w = cnt_w;
	}
	public int getCnt_1() {
		return cnt_1;
	}
	public void setCnt_1(int cnt_1) {
		this.cnt_1 = cnt_1;
	}
	public int getCnt_2() {
		return cnt_2;
	}
	public void setCnt_2(int cnt_2) {
		this.cnt_2 = cnt_2;
	}
	public int getCnt_3() {
		return cnt_3;
	}
	public void setCnt_3(int cnt_3) {
		this.cnt_3 = cnt_3;
	}
	public int getCnt_a() {
		return cnt_a;
	}
	public void setCnt_a(int cnt_a) {
		this.cnt_a = cnt_a;
	}
	public int getCnt_h() {
		return cnt_h;
	}
	public void setCnt_h(int cnt_h) {
		this.cnt_h = cnt_h;
	}
	
}
