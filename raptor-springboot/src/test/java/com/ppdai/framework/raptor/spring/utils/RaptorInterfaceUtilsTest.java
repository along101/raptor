package com.ppdai.framework.raptor.spring.utils;

import com.ppdai.framework.raptor.proto.HelloRequest;
import com.ppdai.framework.raptor.proto.MoreService;
import com.ppdai.framework.raptor.spring.service.MoreServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author yinzuolong
 */
public class RaptorInterfaceUtilsTest {

    @Test
    public void testFindRaptorInterfaces() {
        List<Class<?>> interfaces = RaptorInterfaceUtils.findRaptorInterfaces(MoreServiceImpl.class);
        Assert.assertEquals(1, interfaces.size());
        Assert.assertEquals(MoreService.class, interfaces.get(0));
    }

    @Test
    public void findMethod() {
        Method method = ReflectionUtils.findMethod(MoreServiceImpl.class, "testGet2", HelloRequest.class);
        String interfaceName = RaptorInterfaceUtils.getInterfaceName(MoreServiceImpl.class, method);
        System.out.println(interfaceName);
        Assert.assertEquals(MoreService.class.getName(), interfaceName);
    }
}
