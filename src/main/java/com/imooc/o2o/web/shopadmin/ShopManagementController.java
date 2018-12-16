package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.dto.Result;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;


@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	
	
	/**
	 * 查询指定店铺的商品类别数量
	 * Result<List<ProductCategory>> 用来封装json对象
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
	@ResponseBody
	private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
		//： To be removed  设置临时的shopid,最后是要通过登录以后从session中取获取的
		/*Shop shop = new Shop();
		shop.setShopId(28L);
		request.getSession().setAttribute("currentShop", shop);*/
		
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		List<ProductCategory> list = null;
		if (currentShop != null && currentShop.getShopId() > 0) {
			list = productCategoryService.getProductCategoryList(currentShop.getShopId());
			return new Result<List<ProductCategory>>(true, list);
		} else {
			ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
			return new Result<List<ProductCategory>>(false, ps.getState(), ps.getStateInfo());
		}
	}
	
	
	/**
	 * 批量新增店铺类别信息
	 * @param productCategoryList
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		for (ProductCategory pc : productCategoryList) {
			pc.setShopId(currentShop.getShopId());
		}
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategoryList);
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少输入一个商品类别");
		}
		return modelMap;
	}
	
	
	
	/**
	 * 将此类别下的商品里的类别id置为空，再删除掉该商品类别
	 * @param productCategoryId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (productCategoryId != null && productCategoryId > 0) {
			try {
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId,
						currentShop.getShopId());
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少选择一个商品类别");
		}
		return modelMap;
	}	

	
	/**
	 * 店铺管理信息页面相关操作
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId <= 0) {
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if (currentShopObj == null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			} else {
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		} else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}
	
	
	/**
	 * 分页返回相应店铺列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/getshoplist",method=RequestMethod.GET)
	@ResponseBody
	private  Map<String, Object> getShopList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		/*PersonInfo user = new PersonInfo();
		user.setUserId(1L);
		user.setName("张宇松");
		request.getSession().setAttribute("user", user);*/
		user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
			
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	
	
	/***
	 * 通过店铺Id查询店铺信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/getshopbyid",method=RequestMethod.GET)
	@ResponseBody
	private  Map<String, Object> getShopById(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId > -1) {
			try {
				//:通过店铺Id查询店铺信息
				Shop shop = shopService.getByShopId(shopId);
				//:获取区域列表信息
				List<Area> areaList = areaService.getAreaList();
				//:将查询结果封装在map集合中
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
			
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
	
	/**
	 * 初始化店铺类别列表和区域列表下拉框信息
	 * @return
	 */
	@RequestMapping(value="/getshopinitinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo(){
		 Map<String, Object> modelMap = new HashMap<String, Object>();
		 List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		 List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
		
	}
	
	/**
	 * 注册店铺信息
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/registershop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request){
		 Map<String, Object> modelMap = new HashMap<String, Object>();
		/* String parameter = request.getParameter("verifyCodeActual");
		 System.err.println(parameter);*/
		 //：校验验证码信息
		 if (!CodeUtil.checkVerifyCode(request)) {
			 modelMap.put("success", false);
			 modelMap.put("errMsg", "输入了错误的验证码！");
			 return modelMap;
		}
//		1.接收并转化相应的参数，包括店铺信息以及图片信息
		 String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		 ObjectMapper mapper = new ObjectMapper();
		 Shop shop = null;
		 try {
			 //：转换为shop实体类
			 shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			System.err.println(e.getMessage());
			return modelMap;
		}
		 //：处理上传文件流
		 CommonsMultipartFile shopImg = null;
		 CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		 if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest  =(MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空！");
			return modelMap;
		}
//		2.注册店铺
		 if (shop != null && shopImg != null) {
			 //:从session中获取当前用户的信息
			 PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			 shop.setOwner(owner);
			ImageHolder imageHolder;
			try {
				imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
				ShopExecution se = shopService.addShop(shop, imageHolder);
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					//：从session中取出用户可以操作的店铺列表
					List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
					if (shopList == null || shopList.size()==0) {
						shopList = new ArrayList<Shop>();
					}
					//：将新注册的店铺添加到当前用户的session中去
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList",shopList);
				
				}else{
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息！");
			return modelMap;
		}
	}
	
	
	
	/**
	 * 修改店铺信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/modifyshop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request){
		 Map<String, Object> modelMap = new HashMap<String, Object>();
		
		 //：校验验证码信息
		 if (!CodeUtil.checkVerifyCode(request)) {
			 modelMap.put("success", false);
			 modelMap.put("errMsg", "输入了错误的验证码！");
			 return modelMap;
		}
		 
//		1.接收并转化相应的参数，包括店铺信息以及图片信息
		 String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		 ObjectMapper mapper = new ObjectMapper();
		 Shop shop = null;
		 
		 try {
			 //：转换为shop实体类
			 shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			System.err.println(e.getMessage());
			return modelMap;
		}
		 
		 //：处理上传文件流
		 CommonsMultipartFile shopImg = null;
		 CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		 if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest  =(MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
//		2.修改店铺信息
		 if (shop != null && shop.getShopId() != null) {
			/* PersonInfo owner = new PersonInfo();
			 owner.setUserId(1L);
			 shop.setOwner(owner);*/
			ImageHolder imageHolder;
			ShopExecution se;
			try {
				//:图片是否修改的逻辑处理
				if (shopImg == null) {
					se = shopService.modifyShop(shop, null);
				}else {
					imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
					se = shopService.modifyShop(shop, imageHolder);
				}
				if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else{
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺ID！");
			return modelMap;
		}
	}

}
