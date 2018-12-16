package com.imooc.o2o.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;

public class AreaServiceTest extends BaseTest {
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private CacheService cacheService;

	@Test
	public void test() {
		@SuppressWarnings("unused")
		List<Area> areaList = areaService.getAreaList();
		cacheService.removeFromCache(AreaService.AREALISTKEY);
		areaList = areaService.getAreaList();
		
	}

}
