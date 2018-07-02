package com.ppdai.framework.raptor.benchmark.server.mock;

import com.ppdai.framework.raptor.benchmark.server.httpclient.HttpclientRaptorServerApplication;
import org.apache.http.entity.ContentType;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.SocketUtils;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @author yinzuolong
 */
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MockRaptorServerBenchmark {

    private ConfigurableApplicationContext context;
    private int port;

    private MockMvc mvc;

    @Setup
    public void setup() {
        port = SocketUtils.findAvailableTcpPort();
        System.setProperty("server.port", String.valueOf(port));
        context = SpringApplication.run(HttpclientRaptorServerApplication.class);
        mvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) context).build();
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
        String body = "{\"name\":\"ppdai\"}";
        String url = "/raptor/com.ppdai.framework.raptor.proto.Simple/sayHello";
        String responseBody;
        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post(url).content(body).contentType(ContentType.APPLICATION_JSON.getMimeType()))
                    .andReturn();
            responseBody = result.getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException("error", e);
        }
        bh.consume(responseBody);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MockRaptorServerBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    public static class Main {

        public static void main(String[] args) {
            MockRaptorServerBenchmark clientMockBenchmark = new MockRaptorServerBenchmark();
            clientMockBenchmark.setup();
            clientMockBenchmark.test(new Blackhole("Today's password is swordfish. I understand instantiating Blackholes directly is dangerous."));
            clientMockBenchmark.tearDown();
        }
    }
}
