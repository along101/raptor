package com.ppdai.framework.raptor.spring.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @author yinzuolong
 */
public class FieldUtils {

    public static Object getPrivateField(Class<?> clazz, Object target, String fieldName) {
        Field field = ReflectionUtils.findField(clazz, fieldName);
        if (field != null) {
            ReflectionUtils.makeAccessible(field);
            return ReflectionUtils.getField(field, target);
        }
        return null;
    }
}
