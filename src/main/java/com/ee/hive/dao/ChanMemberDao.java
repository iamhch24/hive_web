package com.ee.hive.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.ee.hive.entities.ChanMember;

@Mapper
public interface ChanMemberDao {
	public ArrayList<ChanMember> selectAll();
	
	public ArrayList<ChanMember> selectOne(String userid);
	
	public void insertRow(ChanMember chanmember);
	
	public void appendChanMember(String chanid, String userid);
	
	public ChanMember selectMember(HashMap<String, String> hashmap);
	
	public void inviteMember(HashMap<String, String> hashmap);
	
	public String selectChanid(HashMap<String, String> hashmap);
	
	public void deleteMember(HashMap<String, String> hashmap);
	
	public Integer selectCount(HashMap<String, String> hashmap);
}
