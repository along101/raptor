package com.ppdai.framework.raptor.benchmark.springmvc.okhttp;

import okhttp3.*;
import org.apache.http.entity.ContentType;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.SocketUtils;

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
public class OkhttpSpringMvcBenchmark {
    private ConfigurableApplicationContext context;
    private int port;
    private final OkHttpClient client = new OkHttpClient();

    @Setup
    public void setup() {
        port = SocketUtils.findAvailableTcpPort();
        System.setProperty("server.port", String.valueOf(port));
        context = SpringApplication.run(OkhttpSpringMvcApplication.class);
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

        Request request = new Request.Builder()
                .url(url)
                .header("connection", "Keep-Alive")
                .post(RequestBody.create(MediaType.parse(ContentType.APPLICATION_JSON.toString()), body.getBytes()))
                .build();
        String responseBody;
        try (Response response = client.newCall(request).execute()) {
            responseBody = new String(response.body().bytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("error", e);
        }
        bh.consume(responseBody);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(OkhttpSpringMvcBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    public static class Main {

        public static void main(String[] args) {
            OkhttpSpringMvcBenchmark clientMockBenchmark = new OkhttpSpringMvcBenchmark();
            clientMockBenchmark.setup();
            clientMockBenchmark.test(new Blackhole("Today's password is swordfish. I understand instantiating Blackholes directly is dangerous."));
            clientMockBenchmark.tearDown();
        }
    }
}
