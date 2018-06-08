package com.kylin.user.aspect;

import com.alibaba.fastjson.JSONObject;
import com.kylin.core.util.ComUtil;
import com.kylin.core.util.StringUtil;
import com.kylin.user.exception.ParamJsonException;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author liugh
 * @since on 2018/5/10.
 */
public class ValidationParam implements AspectApi{
    @Override
    public Object doHandlerAspect(Object [] obj ,ProceedingJoinPoint pjp, Method method,boolean isAll) throws Throwable{
        String validationParamValue = StringUtil.getMethodAnnotationOne(method,ValidationParam.class.getSimpleName());
        if(!ComUtil.isEmpty(validationParamValue)){
            for (int i = 0; i < obj.length; i++) {
                if(obj[i] instanceof JSONObject){
                    JSONObject jsonObject = JSONObject.parseObject(obj[i].toString());
                    hasAllRequired(jsonObject,validationParamValue);
                }else {
                    continue;
                }
            }
        }
        return obj;
    }


    /**
     * 验证前端传入参数,没有抛出异常
     * @param jsonObject
     * @param requiredColumns
     */
    public void hasAllRequired(final JSONObject jsonObject, String requiredColumns) {
        if (!ComUtil.isEmpty(requiredColumns)) {
            //验证字段非空
            String[] columns = requiredColumns.split(",");
            String missCol = "";
            for (String column : columns) {
                Object val = jsonObject.get(column.trim());
                if (ComUtil.isEmpty(val)) {
                    missCol += column + "  ";
                }
            }
            if (!ComUtil.isEmpty(missCol)) {
                jsonObject.clear();
                throw new ParamJsonException("缺少必填参数:"+missCol.trim());
            }
        }
    }
}
