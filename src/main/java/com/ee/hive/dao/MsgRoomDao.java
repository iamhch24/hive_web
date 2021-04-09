package com.ee.hive.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.ee.hive.entities.MsgRoom;

@Mapper
public interface MsgRoomDao {
	public ArrayList<MsgRoom> selectAll();

	public ArrayList<MsgRoom> selectList(HashMap<String, String> hash);
	
	public ArrayList<MsgRoom> selectRoom(String userid);
	
	public ArrayList<String> selectRoomID(String userid, String workid);
	
	public MsgRoom selectmsgroom(String roomseq);
	
	public void insertRow(MsgRoom msgroom);
	
	public MsgRoom selectOne(String roomseq);
	
	public void insertWorkidUseridEmail(HashMap<String, String> hashmap);
	
	public void insertmul(HashMap<String, String> hashmap);
	
	public void deleteRoom(HashMap<String, String> hashmnap);
}
