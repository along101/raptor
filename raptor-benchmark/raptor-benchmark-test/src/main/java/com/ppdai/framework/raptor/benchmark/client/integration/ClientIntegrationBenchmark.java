package com.ppdai.framework.raptor.benchmark.client.integration;

import com.ppdai.framework.raptor.proto.HelloReply;
import com.ppdai.framework.raptor.proto.HelloRequest;
import com.ppdai.framework.raptor.proto.Simple;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.SocketUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author yinzuolong
 */

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ClientIntegrationBenchmark {
    private ConfigurableApplicationContext context;

    private Simple simple;

    @Setup
    public void setup() {
        System.setProperty("server.port", String.valueOf(SocketUtils.findAvailableTcpPort()));
        context = SpringApplication.run(ClientIntegrationApplication.class);
        ClientIntegrationApplication demo = context.getBean(ClientIntegrationApplication.class);
        simple = demo.getSimple();
    }

    @TearDown
    public void tearDown() {
        System.clearProperty("server.port");
        if (context != null) {
            context.close();
        }
    }

    @Benchmark
    public void test(Blackhole bh) {
        HelloRequest helloRequest = new HelloRequest();
        helloRequest.setName("ppdai");
        HelloReply helloReply = simple.sayHello(helloRequest);
        bh.consume(helloReply);
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ClientIntegrationBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    public static class Main {

        public static void main(String[] args) throws RunnerException {
            ClientIntegrationBenchmark clientMockBenchmark = new ClientIntegrationBenchmark();
            clientMockBenchmark.setup();
            clientMockBenchmark.test(new Blackhole("Today's password is swordfish. I understand instantiating Blackholes directly is dangerous."));
            clientMockBenchmark.tearDown();
        }
    }
}
