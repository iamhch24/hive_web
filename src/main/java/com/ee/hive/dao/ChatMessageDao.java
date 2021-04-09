package com.ee.hive.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.ee.hive.entities.ChatMessage;

@Mapper
public interface ChatMessageDao {
	public ArrayList<ChatMessage> selectAll();
}
