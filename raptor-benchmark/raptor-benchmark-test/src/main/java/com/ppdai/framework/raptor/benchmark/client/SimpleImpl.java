package com.ppdai.framework.raptor.benchmark.client;

import com.ppdai.framework.raptor.proto.HelloReply;
import com.ppdai.framework.raptor.proto.HelloRequest;
import com.ppdai.framework.raptor.proto.Simple;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yinzuolong
 */
@RestController
public class SimpleImpl implements Simple {

    @Override
    public HelloReply sayHello(HelloRequest request) {
        String hello = "Hello " + request.getName();
        return new HelloReply(hello, 123, null, null);
    }

}
