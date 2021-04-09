package com.ee.hive.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.ee.hive.entities.User;

@Mapper
public interface UserDao {
	public ArrayList<User> selectAll();

	public User selectOne(String userid);
	
	public User selectEmail(String email);

	public int updateLastWork(HashMap<String, String> hashmap);
	
	public int updateLastChannel(HashMap<String, String> hashmap);
	
	public int updateLastMsgRoom(HashMap<String, String> hashmap);
	
	public String selectLastWork(String userid);
	
	public String selectLastChannel(String userid);
	
	public String selectLastMsgRoom(String userid);
	
	public void insertRow(User user);
	
	public ArrayList<User> selectIdName(String workid);
	
	public ArrayList<User> selectWorkMember(String workid, String userid);
	
	public ArrayList<User> selectMemberIDNamePhoto(String workid);

	public void updateUser(User user);
	
	public void updatePwd(HashMap<String, String> hashmap);
	
	public String selectPwd(String userid);
	
	public void updateAvata(HashMap<String, String> hashmap);
	
	public String selectId(String email);
	
	public ArrayList<User> selectMsgUser(HashMap<String, String> hashmap);
}
