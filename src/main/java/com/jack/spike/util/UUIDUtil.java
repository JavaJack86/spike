package com.jack.spike.util;

import java.util.UUID;
/**
 * @Author Jack
 * @Date 2019/4/26 10:01
 */
public class UUIDUtil {
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
