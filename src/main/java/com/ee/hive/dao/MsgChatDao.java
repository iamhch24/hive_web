package com.ee.hive.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.ee.hive.entities.MsgChat;

@Mapper
public interface MsgChatDao {
	public ArrayList<MsgChat> selectAll();

	public ArrayList<MsgChat> selectMessage(String roomseq);
	
	public MsgChat selectLastMsg(int chatseq);

	public void insertRow(MsgChat msgchat);
}
