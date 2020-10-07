package com.fzx.dianping.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 16:21 2020/2/29
 */
@Configuration
public class ESRestClient {
    @Value("${elasticsearch.ip}")
    String ipAddress;

    @Bean(name = "highLevelClient")
    public RestHighLevelClient highLevelClient() {
        String[] address = ipAddress.split(":");
        String ip = address[0];
        int port = Integer.valueOf(address[1]);
        HttpHost httpHost = new HttpHost(ip, port, "http");
        return new RestHighLevelClient(RestClient.builder(new HttpHost[]{httpHost}));
    }
}
