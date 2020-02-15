package com.tuofan.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class JsonUtils {

	public static <T> T toBean(String json, Class<T> clazz) {
		return JSONObject.parseObject(json, clazz);
	}

	public static <T> List<T> toList(String json, Class<T> clazz) {
		return JSONObject.parseArray(json, clazz);
	}

	public static <T> T toBean(String json, TypeReference<T> type) {
		return JSONObject.parseObject(json, type);
	}

	public static <T> T toBean(String json, Type type) {
		return JSONObject.parseObject(json, type);
	}

	/**
	 * 解决嵌套泛型反序列化
	 * 
	 * List<DataWrapper<User>> list = JsonUtils.toBean(s,JsonUtils.buildType(List.class,DataWrapper.class,User.class))
	 * 
	 * @param types
	 * @return
	 */
	public static Type buildType(Type... types) {
		ParameterizedTypeImpl beforeType = null;
		if (types != null && types.length > 0) {
			for (int i = types.length - 1; i > 0; i--) {
				beforeType = new ParameterizedTypeImpl(new Type[] { beforeType == null ? types[i] : beforeType }, null,
						types[i - 1]);
			}
		}
		return beforeType;
	}

	public static String toJsonString(Object obj) {
		return JSONObject.toJSONString(obj);
	}

	public static Map toMap(String json) {
		return JSONObject.parseObject(json, Map.class);
	}
}
