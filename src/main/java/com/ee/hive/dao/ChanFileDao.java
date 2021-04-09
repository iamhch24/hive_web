package com.ee.hive.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.ee.hive.entities.ChanFile;

@Mapper
public interface ChanFileDao {
	public ArrayList<ChanFile> selectAll();
}
