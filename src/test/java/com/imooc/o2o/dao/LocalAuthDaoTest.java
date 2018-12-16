package com.imooc.o2o.dao;


import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;

public class LocalAuthDaoTest extends BaseTest{
	
	@Autowired
	private LocalAuthDao localAuthDao;

	@Test
	public void testQueryLocalByUserNameAndPwd() {
		
		LocalAuth auth = localAuthDao.queryLocalByUserNameAndPwd("zhangyusong", "123456");
		System.out.println("查询到的用户名是："+auth.getUsername());
		System.out.println("查询到的密码是："+auth.getPassword());
	}

	@Test
	public void testQueryLocalByUserId() {
		
		long userId =2L;
		LocalAuth byUserId = localAuthDao.queryLocalByUserId(userId);
		
	} 

	@Test
	public void testInsertLocalAuth() {
		//:新增一条平台账号信息
		LocalAuth localAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		//:给平台账号绑定上用户信息
		localAuth.setPersonInfo(personInfo);
		localAuth.setUsername("zhangyusong");
		localAuth.setPassword("123456");
		localAuth.setCreateTime(new Date());
		int i = localAuthDao.insertLocalAuth(localAuth);
		System.out.println("成功插入平台账户信息"+i+"条。");
	}

	
	@Test
	public void testUpdateLocalAuth() {
		Date lastEditTime = new Date();
		String newPassword = "627828";
		String password = "123456";
		String username = "zhangyusong";
		Long userId = 1L;
		int i = localAuthDao.updateLocalAuth(userId, username, password, newPassword, lastEditTime);
		System.out.println("成功修改平台账户信息"+i+"条。");
		
	}

}
