package com.ee.hive.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.ee.hive.entities.WorkSpace;

@Mapper
public interface WorkSpaceDao {
	public ArrayList<WorkSpace> selectAll();

	public WorkSpace selectOne(String workid);
	
	public String selectWorkName(String workid);
	
	public ArrayList<WorkSpace> selectWorkspace(String userid);
	
	public void insertRow(WorkSpace workspace);
	
	public String selectOwnerid(String workid);
	
	public WorkSpace selectTableWork(String workid);
	
	public void updateWorkspaceNameOwnerid(WorkSpace workspace);
	
	public void updateWorkOwnerid(String workid);
	
	public void deleteWork(String workid);
	
	public WorkSpace selectWorkNameId(String userid);
	
	public String selectOwneremail(String owneremail);
}
