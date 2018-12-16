package com.imooc.o2o.util;

/**
 *前台页数和后台行数之间的转换
 *因为前台只认页数
 *后台只认行数
 * @author admin
 *
 */
public class PageCalculator {
	public static int calculateRowIndex(int pageIndex, int pageSize) {
		return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
	}
}
