package com.imooc.o2o.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.HeadLine;

public class HeadLineDaoTest extends BaseTest{
	
	@Autowired
	private HeadLineDao headLineDao;

	@Test
	public void testQueryHeadLine() {
		HeadLine headLineCondition = new HeadLine();
		List<HeadLine> queryHeadLine = headLineDao.queryHeadLine(headLineCondition);
		System.out.println(queryHeadLine.size());
	}

	@Test
	public void testQueryHeadLineById() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryHeadLineByIds() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertHeadLine() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateHeadLine() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteHeadLine() {
		fail("Not yet implemented");
	}

	@Test
	public void testBatchDeleteHeadLine() {
		fail("Not yet implemented");
	}

}
