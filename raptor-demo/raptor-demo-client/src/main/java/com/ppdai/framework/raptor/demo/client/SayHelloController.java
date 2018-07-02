package com.ppdai.framework.raptor.demo.client;

import com.ppdai.framework.raptor.proto.HelloRequest;
import com.ppdai.framework.raptor.proto.MoreService;
import com.ppdai.framework.raptor.proto.Simple;
import com.ppdai.framework.raptor.spring.annotation.RaptorClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SayHelloController {

    @RaptorClient
    private Simple simple;

    @RaptorClient
    private MoreService moreService;


    @RequestMapping("/sayhello")
    public Object sayHello(@RequestParam("name") String name) {
        HelloRequest helloRequest = DemoMessageBuilder.getTestRequest();
        helloRequest.setName(name);

        return simple.sayHello(helloRequest);
    }


    @RequestMapping("get")
    public Object testGet1() {
        HelloRequest helloRequest = DemoMessageBuilder.getTestRequest();
        return moreService.testGet1(helloRequest);
    }

    @RequestMapping("get2")
    public Object testGet2() {
        HelloRequest helloRequest = DemoMessageBuilder.getTestRequest();
        return moreService.testGet2(helloRequest);
    }

    @RequestMapping("post1")
    public Object testPost1() {
        HelloRequest helloRequest = DemoMessageBuilder.getTestRequest();
        return moreService.testPost1(helloRequest);
    }

    @RequestMapping("put1")
    public Object testPut1() {
        HelloRequest helloRequest = DemoMessageBuilder.getTestRequest();
        return moreService.testPut1(helloRequest);
    }

    @RequestMapping("delete")
    public Object testDelete1() {
        HelloRequest helloRequest = DemoMessageBuilder.getTestRequest();
        return moreService.testDelete1(helloRequest);
    }
}
