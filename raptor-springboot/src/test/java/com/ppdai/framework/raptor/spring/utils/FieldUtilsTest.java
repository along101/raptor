package com.ppdai.framework.raptor.spring.utils;

import feign.Feign;
import feign.Request;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yinzuolong
 */
public class FieldUtilsTest {

    @Test
    public void testGetPrivateField() {
        Feign.Builder builder = Feign.builder();
        Request.Options options = (Request.Options) FieldUtils.getPrivateField(Feign.Builder.class, builder, "options");
        Assert.assertNotNull(options);
        System.out.println(options.connectTimeoutMillis() + ":" + options.readTimeoutMillis());
    }
}
