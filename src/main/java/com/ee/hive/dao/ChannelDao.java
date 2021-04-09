package com.ee.hive.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.ee.hive.entities.Channel;

public interface ChannelDao {
	public ArrayList<Channel> selectAll();

	public ArrayList<Channel> selectChannel(String userid, String workid);
	
	public ArrayList<String> selectChannelID(String userid, String workid);
	
	public Channel selectOne(String chanid);
	
	public String selectName(String chanid);
	
	public void insertRow(Channel channel);
	
	public void deleteChannel(String chanid);
	
	public void updateNameDescription(Channel channel);
	
	public Channel selectChanNameId(HashMap<String, String> hashmap);
	
	public Integer selectCount(String Workid);
}
