package com.imooc.o2o.service;


import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;

public class ProductCategoryServiceTest extends BaseTest {
	
	@Autowired
	private  ProductCategoryService productCategoryService;

	@Test
	public void test() {
		long shopId =28L;
		List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(shopId);
		System.out.println("该指定店铺下的商品类别数量为："+productCategoryList.size());
	}

}
