package com.ppdai.framework.raptor.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;

public class CommonSelfIdGeneratorTest {

    @Test
    public void testThreadSafe() throws InterruptedException, ExecutionException {
        int nThread = 10;
        int idPerThread = 1000;

        CommonSelfIdGenerator commonSelfIdGenerator = new CommonSelfIdGenerator();


        CountDownLatch countDownLatch = new CountDownLatch(nThread);

        Callable<List<Long>> generate = () -> {
            ArrayList<Long> ids = new ArrayList<>();
            countDownLatch.countDown();
            countDownLatch.await();
            for (int i = 0; i < idPerThread; i++) {
                long l = commonSelfIdGenerator.generateId();
                ids.add(l);
            }
            return ids;
        };

        ExecutorService executorService = Executors.newFixedThreadPool(nThread);
        List<Future<List<Long>>> futureList = new ArrayList<>();
        for (int i = 0; i < nThread; i++) {
            Future<List<Long>> futureResult = executorService.submit(generate);
            futureList.add(futureResult);
        }
        countDownLatch.await();

        HashSet<Long> allIds = new HashSet<>();
        for (Future<List<Long>> result : futureList) {
            List<Long> longs = result.get();
            allIds.addAll(longs);
        }

        Assert.assertEquals(allIds.size(), nThread * idPerThread);


    }


}
