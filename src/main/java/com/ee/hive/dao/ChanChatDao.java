package com.ee.hive.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.ee.hive.entities.ChanChat;

@Mapper
public interface ChanChatDao {
	public ArrayList<ChanChat> selectAll();

	public ArrayList<ChanChat> selectMessage(String chanid);

	public void insertRow(ChanChat chanchat);
	
}