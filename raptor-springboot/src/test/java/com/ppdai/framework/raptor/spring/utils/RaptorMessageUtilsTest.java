package com.ppdai.framework.raptor.spring.utils;

import com.ppdai.framework.raptor.proto.Cat;
import com.ppdai.framework.raptor.proto.HelloRequest;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yinzuolong
 */
public class RaptorMessageUtilsTest {

    @Test
    public void testSimple() {
        HelloRequest request = new HelloRequest();
        request.setName("ppdai");
        request.setTbytes("拍拍贷".getBytes(StandardCharsets.UTF_8));
        request.setTDouble(1.01);
        request.setTFloat(1.02f);
        request.setTint32(123);
        request.setTint64(3232132555244324324L);
        request.setTbool(true);

        Map<String, String> map = RaptorMessageUtils.transferMessageToMap(request);

        HelloRequest request1 = RaptorMessageUtils.transferMapToMessage(HelloRequest.class, map);
        Assert.assertEquals(request, request1);
    }


    @Test
    public void testEnum() {
        HelloRequest request = new HelloRequest();
        request.setCorpus(HelloRequest.Corpus.UNIVERSAL);

        Map<String, String> map = RaptorMessageUtils.transferMessageToMap(request);

        HelloRequest request1 = RaptorMessageUtils.transferMapToMessage(HelloRequest.class, map);
        Assert.assertEquals(request, request1);
    }

    @Test
    public void testMessage() {
        HelloRequest request = new HelloRequest();
        request.setResult(new HelloRequest.Result("http://ppdai.com", HelloRequest.Result.Corpus.NEWS));

        Map<String, String> map = RaptorMessageUtils.transferMessageToMap(request);

        HelloRequest request1 = RaptorMessageUtils.transferMapToMessage(HelloRequest.class, map);
        Assert.assertEquals(request, request1);
    }

    @Test
    public void testListString() {
        HelloRequest request = new HelloRequest();
        request.setRepString(Arrays.asList("str1", "str2", "str3"));
        HelloRequest.Result result = new HelloRequest.Result("url", HelloRequest.Result.Corpus.LOCAL);
        request.setRepResult(Arrays.asList(result, result, result));

        Map<String, String> map = RaptorMessageUtils.transferMessageToMap(request);

        HelloRequest request1 = RaptorMessageUtils.transferMapToMessage(HelloRequest.class, map);
        Assert.assertEquals(request, request1);
    }

    @Test
    public void testListMessage() {
        HelloRequest request = new HelloRequest();
        request.setCats(Arrays.asList(new Cat("black"), new Cat("white")));

        Map<String, String> map = RaptorMessageUtils.transferMessageToMap(request);

        HelloRequest request1 = RaptorMessageUtils.transferMapToMessage(HelloRequest.class, map);
        Assert.assertEquals(request, request1);
    }

    @Test
    public void testMapIntInt() {
        HelloRequest request = new HelloRequest();
        HashMap<Integer, Integer> intMap = new HashMap<>();
        intMap.put(1, 2);
        intMap.put(3, 4);
        request.setMapInt32Int32(intMap);

        Map<String, String> map = RaptorMessageUtils.transferMessageToMap(request);

        HelloRequest request1 = RaptorMessageUtils.transferMapToMessage(HelloRequest.class, map);
        Assert.assertEquals(request, request1);
    }

    @Test
    public void testMapStringMessage() {
        HelloRequest request = new HelloRequest();
        HelloRequest.Result result = new HelloRequest.Result("url1", HelloRequest.Result.Corpus.NEWS);
        HashMap<String, HelloRequest.Result> stringMessageMap = new HashMap<>();
        stringMessageMap.put("c1", result);
        stringMessageMap.put("c2", result);
        request.setMapStringMessage(stringMessageMap);

        Map<String, String> map = RaptorMessageUtils.transferMessageToMap(request);

        HelloRequest request1 = RaptorMessageUtils.transferMapToMessage(HelloRequest.class, map);
        Assert.assertEquals(request, request1);
    }

    @Test
    public void testMapStringEnum() {
        HelloRequest request = new HelloRequest();
        HashMap<String, HelloRequest.Corpus> stringEnumMap = new HashMap<>();
        stringEnumMap.put("c1", HelloRequest.Corpus.IMAGES);
        stringEnumMap.put("c2", HelloRequest.Corpus.PRODUCTS);
        request.setMapStringEnum(stringEnumMap);

        Map<String, String> map = RaptorMessageUtils.transferMessageToMap(request);

        HelloRequest request1 = RaptorMessageUtils.transferMapToMessage(HelloRequest.class, map);
        Assert.assertEquals(request, request1);
    }
}
