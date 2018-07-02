package com.ppdai.framework.raptor.benchmark.server.httpclient;

import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.SocketUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author yinzuolong
 */

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class HttpclientRaptorServerBenchmark {
    private ConfigurableApplicationContext context;
    private int port;
    private CloseableHttpClient httpClient;

    @Setup
    public void setup() {
        port = SocketUtils.findAvailableTcpPort();
        System.setProperty("server.port", String.valueOf(port));
        context = SpringApplication.run(HttpclientRaptorServerApplication.class);
        httpClient = context.getBean(CloseableHttpClient.class);
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
        String url = "http://localhost:" + port + "/raptor/com.ppdai.framework.raptor.proto.Simple/sayHello";
        HttpUriRequest request = RequestBuilder.post(url)
                .addHeader("connection", "Keep-Alive")
                .setEntity(EntityBuilder.create().setText(body).setContentType(ContentType.APPLICATION_JSON).build())
                .build();
        String responseBody;
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(100);
            response.getEntity().writeTo(bos);
            responseBody = new String(bos.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("error", e);
        }
        bh.consume(responseBody);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(HttpclientRaptorServerBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    public static class Main {

        public static void main(String[] args) {
            HttpclientRaptorServerBenchmark clientMockBenchmark = new HttpclientRaptorServerBenchmark();
            clientMockBenchmark.setup();
            clientMockBenchmark.test(new Blackhole("Today's password is swordfish. I understand instantiating Blackholes directly is dangerous."));
            clientMockBenchmark.tearDown();
        }
    }
}
