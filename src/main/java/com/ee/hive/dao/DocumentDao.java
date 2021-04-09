package com.ee.hive.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.ee.hive.entities.Document;
import com.ee.hive.entities.DocumentRollUp;

@Mapper
public interface DocumentDao {
	public ArrayList<Document> selectAll();

	public Document selectOne(String docuseq);

	public ArrayList<Document> selectFromMe(String userid, String workid);

	public ArrayList<Document> selectToMe(String userid, String workid);

	public void insertRow(Document docu);

	public void updateRow(Document docu);

	public void deleteRow(String docuseq);
	
	public DocumentRollUp rollupFromMe(String userid, String workid);

	public DocumentRollUp rollupToMe(String userid, String workid);

}
