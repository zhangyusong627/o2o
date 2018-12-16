package com.imooc.o2o.util;

import redis.clients.jedis.Jedis;

public class redisTest {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Jedis jedis = new Jedis("127.0.0.1");
		System.out.println("Connection to server sucessfully");
		String a = jedis.set("name","zhangsan");
        String b = jedis.get("name");
       System.out.print("name的值："+b);
	}

}
