package com.imooc.o2o.service;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;

public class LocalAuthServiceTest  extends BaseTest{
	
	@Autowired
	private LocalAuthService localAuthService;

	@Test
	public void testGetLocalAuthByUsernameAndPwd() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLocalAuthByUserId() {
		fail("Not yet implemented");
	}

	@Test
	public void testBindLocalAuth() {
		LocalAuth localAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		String username = "zhangyusong";
		String password = "12345678";
		// 给平台帐号设置上用户信息
		// 给用户设置上用户Id,标明是某个用户创建的帐号
		personInfo.setUserId(8L);
		// 给平台帐号设置用户信息,标明是与哪个用户绑定
		localAuth.setPersonInfo(personInfo);
		// 设置帐号
		localAuth.setUsername(username);
		// 设置密码
		localAuth.setPassword(password);
		LocalAuthExecution bindLocalAuth = localAuthService.bindLocalAuth(localAuth);
		System.out.println(bindLocalAuth.getLocalAuth().getUsername());
	}

	@Test
	public void testModifyLocalAuth() {
		fail("Not yet implemented");
	}

}
