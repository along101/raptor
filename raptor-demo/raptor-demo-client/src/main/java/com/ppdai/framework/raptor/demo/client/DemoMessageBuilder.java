package com.ppdai.framework.raptor.demo.client;

import com.ppdai.framework.raptor.proto.Cat;
import com.ppdai.framework.raptor.proto.HelloRequest;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author yinzuolong
 */
public class DemoMessageBuilder {

    public static HelloRequest getTestRequest() {
        HelloRequest helloRequest = new HelloRequest();
        helloRequest.setCorpus(HelloRequest.Corpus.UNIVERSAL);
        helloRequest.setSnippets(Arrays.asList("snippets1", "snippets2"));
        helloRequest.setCats(Arrays.asList(new Cat("black"), new Cat("white")));
        helloRequest.setResult(new HelloRequest.Result("http://ppdai.com", HelloRequest.Result.Corpus.NEWS));
        helloRequest.setTbytes("拍拍贷".getBytes(StandardCharsets.UTF_8));
        helloRequest.setTDouble(1.01);
        helloRequest.setTFloat(1.02f);
        helloRequest.setTfixed32(23);
        helloRequest.setTfixed64(23232222332332323L);
        helloRequest.setTint32(123);
        helloRequest.setTint64(3232132555244324324L);
        helloRequest.setTsfixed32(213);
        helloRequest.setTsfixed64(323232323223232L);
        helloRequest.setTunit32(123);
        helloRequest.setTunit64(323213232133223L);
        helloRequest.setTbool(true);

        helloRequest.setRepFixed32(Arrays.asList(1, 2, 3));
        helloRequest.setRepString(Arrays.asList("str1", "str2"));

        HelloRequest.Result result = new HelloRequest.Result("url1", HelloRequest.Result.Corpus.NEWS);
        helloRequest.setRepResult(Arrays.asList(result, result, result));

        HashMap<Integer, Integer> intMap = new HashMap<>();
        intMap.put(1, 2);
        intMap.put(3, 4);
        helloRequest.setMapInt32Int32(intMap);

        HashMap<String, HelloRequest.Result> stringMessageMap = new HashMap<>();
        stringMessageMap.put("c1", result);
        stringMessageMap.put("c2", result);
        helloRequest.setMapStringMessage(stringMessageMap);

        HashMap<String, HelloRequest.Corpus> stringEnumMap = new HashMap<>();
        stringEnumMap.put("c1", HelloRequest.Corpus.IMAGES);
        stringEnumMap.put("c2", HelloRequest.Corpus.PRODUCTS);
        helloRequest.setMapStringMessage(stringMessageMap);
        helloRequest.setMapStringEnum(stringEnumMap);
        return helloRequest;
    }
}
