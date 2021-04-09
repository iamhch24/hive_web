package com.ee.hive.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.ee.hive.entities.Avatas;

@Mapper
public interface AvatasDao {
	public ArrayList<Avatas> selectAll();
}
