package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	
	@Autowired
	private ShopDao shopDao;
	
	/**
	 *添加店铺信息
	 */
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder imageHolder) {
		//:空值判断
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			//：给店铺信息赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			//：添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum<=0) {
				throw new ShopOperationException("店铺创建失败！");
			} else{
				if (imageHolder.getImage() != null) {
					try {
						//:存储图片
						addShopImg(shop,imageHolder);
						//shop.getShopImg();
					} catch (Exception e) {
						throw new ShopOperationException("addShopImg Erro:"+e.getMessage());
					}
					//:更新图片地址信息
					effectedNum =shopDao.updateShop(shop);
					if (effectedNum <=0){
						throw new ShopOperationException("更新图片地址失败！");
					}
					
				}
			}
			
		} catch (Exception e) {
			throw new ShopOperationException("addShop Erro:"+e.getMessage());
		}
		
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}
	
	/**
	 * 添加店铺的图片信息【辅助方法】
	 * @param shop
	 * @param imageHolder
	 */
	private void addShopImg(Shop shop, ImageHolder imageHolder) {
		//：获取shop图片目录的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(imageHolder, dest);
		//：将图片地址存放在图片属性字段中
		shop.setShopImg(shopImgAddr);
	}

	@Override
	public Shop getByShopId(long shopId) throws ShopOperationException {
		return shopDao.queryByShopId(shopId);
	}
	
	/**
	 * 更新店铺信息
	 */
	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder imageHolder)
			throws ShopOperationException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}else {
			try {

				// 1.判断是否有文件或者图片流信息需要处理
				
				//：如果前端有传新的图片信息过来
				if (imageHolder.getImage() != null && imageHolder.getImageName() !=null && !"".equals(imageHolder.getImageName())) {
					//：则通过shopId获取当前正在被修改店铺的原图片信息
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if (tempShop.getShopImg()!=null) {
						//:调用图片处理工具类，删除原来的图片存储信息及其文件夹
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					//:将信息的图片信息封装到shop对象中
					addShopImg(shop, imageHolder);
				}
				
				// 2.更新店铺信息
				
				shop.setLastEditTime(new Date());//:设置最后的更新时间
				// 调用更新店铺信息的DAO层方法
				int effecteNum = shopDao.updateShop(shop);
				if (effecteNum <= 0) {
					//:如果更新失败，则抛出异常信息
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else{
					//：如果更新成功，则将最新的更新信息回显到页面
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS,shop);
				}
			} catch (Exception e) {
				throw new ShopOperationException("modifyShop error:"+e.getMessage());
			}
		}
	}
	
	
	/**
	 * 根据shopCondition分页返回相应店铺列表
	 */
	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if (shopList != null) {
			se.setShopList(shopList);
			se.setCount(count);
		}else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

}
