package com.imooc.o2o.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;

public class ShopDaoTest extends BaseTest {

	@Autowired
	private ShopDao shopDao;

	@Test
	public void testQueryShopCount() {

		Shop shopCondition = new Shop();
		PersonInfo owner = new PersonInfo();
		owner.setUserId(1L);
		shopCondition.setOwner(owner);

		int queryShopCount = shopDao.queryShopCount(shopCondition);
		System.out.println("店铺的总数：" + queryShopCount);
	}

	@Test
	public void testQueryShopList() {
		Shop shopCondition = new Shop();
		PersonInfo owner = new PersonInfo();
		owner.setUserId(1L);
		shopCondition.setOwner(owner);
		// ：换条件查询
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryId(22L);
		shopCondition.setShopCategory(shopCategory);
		
		List<Shop> queryShopList = shopDao.queryShopList(shopCondition, 0, 5);
		for (Shop shop : queryShopList) {
			System.out.println(shop);
		}
		
		System.out.println("查询出的店铺列表大小为：" + queryShopList.size());
		System.out.println("ShopCategoryId为22的店铺总数："+queryShopList.size());

	}

	@Test
	public void testQueryShopById() {
		long shopId = 28L;
		Shop queryByShopId = shopDao.queryByShopId(shopId);
		System.out.println(queryByShopId);
		System.out.println(queryByShopId.getShopName());
	}

	@Test
	public void testInsertShop() {

		Shop shop = new Shop();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		PersonInfo owner = new PersonInfo();
		/**
		 * 插入数据时必须要注意关联的其他类的信息 如果其他类不存在这个数据，贸然插入会导致外键报错
		 */
		area.setAreaId(2);
		owner.setUserId(1L);
		shopCategory.setShopCategoryId(10L);

		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopAddr("test");
		shop.setShopName("测试店铺");
		shop.setPhone("18871189607");
		shop.setShopDesc("test");
		shop.setShopImg("test");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(1);
		shop.setAdvice("审核中");
		shop.setPriority(1);
		shop.setLastEditTime(new Date());
		int i = shopDao.insertShop(shop);
		System.out.println(i);
	}

	@Test
	public void testUpdateShop() {
		Shop shop = new Shop();
		shop.setShopId(38L);
		shop.setShopDesc("测试更新描述");
		shop.setShopAddr("测试更新地址");
		shop.setLastEditTime(new Date());
		int updateShop = shopDao.updateShop(shop);
		System.out.println("影响行数是：" + updateShop);
	}

}
