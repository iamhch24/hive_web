package com.ee.hive.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.ee.hive.entities.WorkMember;

@Mapper
public interface WorkMemberDao {
	public ArrayList<WorkMember> selectAll(String workid);

	public ArrayList<WorkMember> selectOne(String userid);
	
	public void insertRow(WorkMember workmember);
	
	public void inviteMember(HashMap<String, String> hashmap);
	
	public void deleteWorkMember(HashMap<String, String> hashmap);
	
	public WorkMember selectMember(HashMap<String, String> hashmap);
	
	public Integer selectCount(String userid);
	
	public ArrayList<String> selectAllId(String workid);
}
