package com.ppdai.framework.raptor.demo.client;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.ppdai.framework.raptor.proto.HelloReply;
import com.ppdai.framework.raptor.proto.HelloRequest;
import com.ppdai.framework.raptor.proto.Simple;
import com.ppdai.framework.raptor.spring.annotation.RaptorClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yinzuolong
 */
@RestController
public class PerformanceTestController {

    @RaptorClient
    private Simple simple;

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    @RequestMapping("/thread")
    public String test(@RequestParam("threadCount") int threadCount,
                       @RequestParam("second") int second,
                       @RequestParam("data") String data) throws Exception {
        if (isRunning.compareAndSet(false, true)) {
            try {
                MetricRegistry registry = new MetricRegistry();
                Timer timer = registry.timer("test.timer");
                ExecutorService pool = new ThreadPoolExecutor(threadCount + 10,
                        threadCount + 10,
                        0L,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());

                //预热
                for (int i = 0; i < 10; i++) {
                    rpcCall(data);
                }

                //开启线程一起跑
                CountDownLatch countDownLatch = new CountDownLatch(threadCount);
                for (int t = 0; t < threadCount; t++) {
                    pool.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                countDownLatch.await();
                            } catch (InterruptedException ignored) {
                            }
                            while (isRunning.get()) {
                                Timer.Context tc = timer.time();
                                rpcCall(data);
                                tc.stop();
                            }
                        }
                    });
                    //保证10个线程一起启动
                    countDownLatch.countDown();
                }
                TimeUnit.SECONDS.sleep(second);
                pool.shutdownNow();
                return getMetricReport(registry);
            } finally {
                isRunning.set(false);
            }
        }
        return "is running now.";
    }

    private String getMetricReport(MetricRegistry registry) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outputStream);
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
                .outputTo(ps)
                .build();
        reporter.report();
        return new String(outputStream.toByteArray());
    }

    private String rpcCall(String data) {
        HelloRequest helloRequest = new HelloRequest();
        helloRequest.setName(data);
        HelloReply reply = simple.sayHello(helloRequest);
        return reply.getMessage();
    }
}
