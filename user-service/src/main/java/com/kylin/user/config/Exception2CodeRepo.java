package com.kylin.user.config;

import com.kylin.user.common.RestCode;
import com.kylin.user.exception.IllegalParamsException;
import com.kylin.user.exception.UserException;
import com.kylin.user.exception.WithTypeException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.common.collect.ImmutableMap;

public class Exception2CodeRepo {
	
	private static final ImmutableMap<Object, RestCode> MAP = ImmutableMap.<Object, RestCode>builder()
			.put(IllegalParamsException.Type.WRONG_PAGE_NUM,RestCode.WRONG_PAGE)
            .put(IllegalStateException.class,RestCode.UNKNOWN_ERROR)
            .put(UserException.class,RestCode.UNKNOWN_ERROR)
	        .build();

	private static Object getType(Throwable throwable){
	   try {
		  return FieldUtils.readDeclaredField(throwable, "type", true);
	   } catch (Exception e) {
		  return null;
	   }	
	}
	
	
	public static RestCode getCode(Throwable throwable) {
		if (throwable == null) {
			return RestCode.UNKNOWN_ERROR;
		}
		Object target = throwable;
		if (throwable instanceof WithTypeException) {
			Object type = getType(throwable);
			if (type != null ) {
				target = type;
			}
		}
		RestCode restCode =  MAP.get(target);
		if (restCode != null) {
			return restCode;
		}
		//获取最底层的异常
		Throwable rootCause = ExceptionUtils.getRootCause(throwable);
		if (rootCause != null) {
			return getCode(rootCause);
		}
		return restCode.UNKNOWN_ERROR;
	}

}
