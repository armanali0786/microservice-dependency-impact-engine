package com.dependencyimpact.impactanalysis.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

// Without an explicit timeout, GraphServiceClient's .block() call could hang forever
// if dependency-graph-service stops responding - and since that call happens inside
// the Kafka consumer thread (ImpactWorker), a hang there stalls the whole consumer,
// not just one request. 5s is generous for a bounded-depth graph query on this MVP's
// data size; real circuit-breaking/retry is deferred to the Resilience4j milestone.
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
