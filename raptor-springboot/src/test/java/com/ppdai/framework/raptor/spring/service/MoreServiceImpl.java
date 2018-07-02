package com.ppdai.framework.raptor.spring.service;

import com.ppdai.framework.raptor.exception.RaptorException;
import com.ppdai.framework.raptor.proto.HelloReply;
import com.ppdai.framework.raptor.proto.HelloRequest;
import com.ppdai.framework.raptor.proto.MoreService;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yinzuolong
 */
@RestController
public class MoreServiceImpl implements MoreService {

    @Override
    public HelloReply testGet1(HelloRequest request) {
        Map<String, String> result = new HashMap<>();
        return new HelloReply("testGet1", 123, request, result);
    }

    @Override
    public HelloReply testGet2(HelloRequest request) {
        if ("error".equals(request.getName())) {
            throw new RuntimeException("error!");
        }
        if ("RaptorException".equals(request.getName())) {
            HashMap<String, String> map = new HashMap<>();
            map.put("a", "b");
            throw new RaptorException(1001, "RaptorException!", map, null);
        }
        Map<String, String> result = new HashMap<>();
        return new HelloReply("testGet2", 123, request, result);
    }

    @Override
    public HelloReply testPost1(HelloRequest request) {
        Map<String, String> result = new HashMap<>();
        return new HelloReply("testPost1", 123, request, result);
    }

    @Override
    public HelloReply testPut1(HelloRequest request) {
        Map<String, String> result = new HashMap<>();
        return new HelloReply("testPut1", 123, request, result);
    }

    @Override
    public HelloReply testDelete1(HelloRequest request) {
        Map<String, String> result = new HashMap<>();
        return new HelloReply("testDelete1", 123, request, result);
    }
}
