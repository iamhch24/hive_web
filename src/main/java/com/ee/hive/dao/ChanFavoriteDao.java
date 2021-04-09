package com.ee.hive.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.ee.hive.entities.ChanFavorite;

@Mapper
public interface ChanFavoriteDao {
	public ArrayList<ChanFavorite> selectAll();
}
