package com.imooc.o2o.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;

public class ShopServiceTest extends BaseTest {
	
	@Autowired
	private ShopService shopService;
	
	@Test
	public void testGetShopList() {
		
		
		Shop shopCondition = new Shop();
		PersonInfo owner = new PersonInfo();
		owner.setUserId(1L);
		shopCondition.setOwner(owner);
		ShopExecution shopList = shopService.getShopList(shopCondition, 2, 5);
		System.out.println(shopList.getShopList().size());
		System.out.println(shopList.getCount());
		
		
	}
	

	@Test
	public void test() throws FileNotFoundException {
		Shop shop = new Shop();Area area = new Area();
		ShopCategory shopCategory = new  ShopCategory(); 
		PersonInfo owner = new PersonInfo();
		area.setAreaId(2);
		owner.setUserId(1L);
		shopCategory.setShopCategoryId(33L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopAddr("test1");
		shop.setShopName("测试店铺1");
		shop.setPhone("test1");
		shop.setShopDesc("test1");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		shop.setPriority(1);
		shop.setLastEditTime(new Date());
		File shopImg = new File("D:/projectdev/image/2017091621545314507.jpg");
		FileInputStream inputStream = new FileInputStream(shopImg);
		ImageHolder thumbnail = new ImageHolder(shopImg.getName(), inputStream);
		ShopExecution shopExecution = shopService.addShop(shop, thumbnail );
		System.out.println(shopExecution);
	}

}
