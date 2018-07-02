package com.ppdai.framework.raptor.spring.utils;

import com.ppdai.framework.raptor.annotation.RaptorField;
import com.ppdai.framework.raptor.annotation.RaptorMessage;
import com.ppdai.framework.raptor.spring.service.ByteArrayBase64PropertyEditor;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author yinzuolong
 */
public class RaptorMessageUtils {

    private static Set<Class<?>> mapKeyClasses = new HashSet<>();

    static {
        mapKeyClasses.add(Double.class);
        mapKeyClasses.add(Float.class);
        mapKeyClasses.add(Integer.class);
        mapKeyClasses.add(Long.class);
        mapKeyClasses.add(Boolean.class);
        mapKeyClasses.add(String.class);
    }

    public static MutablePropertyValues toPropertyValues(Map<String, String> map) {
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                propertyValues.add(entry.getKey(), entry.getValue());
            }
        }
        return propertyValues;
    }

    @SuppressWarnings("unchecked")
    public static <T> T transferMapToMessage(Class<T> clazz, Map<String, String> map) {
        MutablePropertyValues propertyValues = toPropertyValues(map);
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(clazz);
        beanWrapper.registerCustomEditor(byte[].class, new ByteArrayBase64PropertyEditor());
        beanWrapper.setAutoGrowNestedPaths(true);
        beanWrapper.setPropertyValues(propertyValues);
        return (T) beanWrapper.getWrappedInstance();
    }

    public static Map<String, String> transferMessageToMap(Object message) {
        RaptorMessage raptorMessage = AnnotationUtils.findAnnotation(message.getClass(), RaptorMessage.class);
        Assert.notNull(raptorMessage, "Object not annotated by @RaptorMessage.");
        Map<String, String> resultMap = new HashMap<>();
        transferMessageToMap("", resultMap, message);
        return resultMap;
    }

    private static void transferMessageToMap(String name, Map<String, String> resultMap, Object value) {
        if (value == null) {
            return;
        }
        if (List.class.isAssignableFrom(value.getClass())) {
            //List
            List valueList = (List) value;
            for (int i = 0; i < valueList.size(); i++) {
                transferMessageToMap(getNameKey(name, String.valueOf(i)), resultMap, valueList.get(i));
            }
        } else if (Map.class.isAssignableFrom(value.getClass())) {
            //Map
            Map valueMap = (Map) value;
            for (Object key : valueMap.keySet()) {
                if (!mapKeyClasses.contains(key.getClass())) {
                    continue;
                }
                transferMessageToMap(getNameKey(name, key.toString()), resultMap, valueMap.get(key));
            }
        } else if (isAnnotatedByRaptorMessage(value.getClass())) {
            //Message类型
            Field[] fields = value.getClass().getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                RaptorField wireField = AnnotationUtils.findAnnotation(field, RaptorField.class);
                if (wireField == null) {
                    continue;
                }
                Object filedValue = FieldUtils.getPrivateField(value.getClass(), value, fieldName);
                if (filedValue == null) {
                    continue;
                }
                transferMessageToMap(getName(name, fieldName), resultMap, filedValue);
            }
        } else if (byte[].class.isAssignableFrom(value.getClass())) {
            //byte[]类型,base64编码
            resultMap.put(name, Base64Utils.encodeToString((byte[]) value));
        } else {
            //叶子节点，直接放入map
            resultMap.put(name, value.toString());
        }
    }

    private static String getNameKey(String name, String key) {
        return name + "[" + key + "]";
    }

    private static String getName(String parent, String name) {
        if (StringUtils.isEmpty(parent)) {
            return name;
        }
        return parent + "." + name;
    }

    private static boolean isAnnotatedByRaptorMessage(Class<?> type) {
        return AnnotationUtils.findAnnotation(type, RaptorMessage.class) != null;
    }
}
