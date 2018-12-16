package com.imooc.o2o.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;

public class ProductCategoryDaoTest extends BaseTest{
	
	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Test
	public void testQueryProductCategoryList() {
		Long shopId =28L;
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		System.out.println("该店铺的自定义类别数："+productCategoryList.size());
	}
	
	
	
	@Test
	public void testbatchInsertProductCategory() {
		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		
		ProductCategory productCategory1 = new ProductCategory();
		productCategory1.setCreateTime(new Date());
		productCategory1.setPriority(2);
		productCategory1.setShopId(1L);
		productCategory1.setProductCategoryName("猫屎咖啡测试");
		
		
		ProductCategory productCategory2 = new ProductCategory();
		productCategory2.setCreateTime(new Date());
		productCategory2.setPriority(1);
		productCategory2.setShopId(1L);
		productCategory2.setProductCategoryName("皇家鸡排测试");
		
		productCategoryList.add(productCategory1);
		productCategoryList.add(productCategory2);
		
		int batchInsertProductCategory = productCategoryDao.batchInsertProductCategory(productCategoryList);
		System.out.println("插入的数据为："+batchInsertProductCategory+"条。");
	}

}
