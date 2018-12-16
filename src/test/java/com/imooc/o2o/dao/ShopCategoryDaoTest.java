package com.imooc.o2o.dao;


import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ShopCategory;

public class ShopCategoryDaoTest extends BaseTest{
	
	@Autowired
	private ShopCategoryDao shopCategoryDao;

	@Test
	public void testQueryShopCategory() {
		
		/*ShopCategory shopCategoryCondition = new ShopCategory() ;
		List<ShopCategory> shopCategory = shopCategoryDao.queryShopCategory(shopCategoryCondition);
		System.out.println(shopCategory.size());
		for (ShopCategory shopCategory2 : shopCategory) {
			System.out.println(shopCategory2.getShopCategoryName());
		}*/
		
		List<ShopCategory> queryShopCategory = shopCategoryDao.queryShopCategory(null);
		System.out.println(queryShopCategory.size()==6);
		
	}

}
