package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;

public interface ShopService {
	
	/**
	 * 根据shopCondition分页返回相应店铺列表
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);
	
	/**
	 * 添加店铺信息
	 * @param shop       商店参数信息
	 * @param thumbnail  图片文件流
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution addShop(Shop shop ,ImageHolder imageHolder)throws ShopOperationException;
	
	/**
	 * 通过店铺Id查询店铺信息
	 * @param shopId
	 * @return
	 * @throws ShopOperationException
	 */
	Shop getByShopId(long shopId)throws ShopOperationException;
	
	
	/**
	 * 更新店铺信息，包括对图片的处理
	 * 
	 * @param shop
	 * @param shopImg
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;

}
