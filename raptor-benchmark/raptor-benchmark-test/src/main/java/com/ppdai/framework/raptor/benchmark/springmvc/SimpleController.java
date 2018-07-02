package com.ppdai.framework.raptor.benchmark.springmvc;

import com.ppdai.framework.raptor.proto.HelloReply;
import com.ppdai.framework.raptor.proto.HelloRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yinzuolong
 */
@RestController
public class SimpleController {


    @RequestMapping(
            path = "/raptor/com.ppdai.framework.raptor.proto.Simple/sayHello",
            method = RequestMethod.POST
    )
    public HelloReply sayHello(HelloRequest request) {
        String hello = "Hello " + request.getName();
        return new HelloReply(hello, 123, null, null);
    }
}
