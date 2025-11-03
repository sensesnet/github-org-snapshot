package org.snapshot.github.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.snapshot.github.service.GetGitHubRepoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class ServiceConfiguration {
    @Bean
    public RestTemplate restTemplate(
            @Value("${rest.template.connect-timeout:3s}") Duration connectTimeout,
            @Value("${rest.template.read-timeout:5s}") Duration readTimeout,
            @Value("${rest.template.max-conn-total:50}") int maxConnTotal,
            @Value("${rest.template.max-conn-per-route:20}") int maxConnPerRoute,
            @Value("${rest.template.connection-ttl:5m}") Duration connectionTtl
    ) {
        var connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(maxConnTotal)
                .setMaxConnPerRoute(maxConnPerRoute)
                .setConnectionTimeToLive(TimeValue.of(connectionTtl))
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.of(connectTimeout))
                        .setResponseTimeout(Timeout.of(readTimeout))
                        .build())
                .build();

        var requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }

    @Bean
    public GetGitHubRepoService getGitHubRepoService(
            @Value("${github.api.base-url}") String baseUrl,
            @Value("${github.api.user-agent}") String userAgent,
            @Value("${github.api.per-page}") int perPage,
            RestTemplate restTemplate
    ) {
        return new GetGitHubRepoService(baseUrl,
                userAgent,
                perPage,
                restTemplate);
    }
}
