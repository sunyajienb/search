package com.sun.search.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URL;

/**
 * @class: ElasticsearchConfig
 * @description: ES配置文件
 * @author: Jay Sun
 * @time: 2019-02-05 18:14
 **/
@Configuration
@Slf4j
public class ElasticsearchConfig implements FactoryBean<RestHighLevelClient>, InitializingBean, DisposableBean {

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    private RestHighLevelClient restHighLevelClient;

    @Override
    public void destroy() {
        try {
            if (restHighLevelClient != null) {
                restHighLevelClient.close();
            }
        } catch (IOException e) {
            log.error("Error closing ElasticSearch client: ", e);
        }
    }

    @Override
    public RestHighLevelClient getObject() {
        return restHighLevelClient;
    }

    @Override
    public Class<?> getObjectType() {
        return RestHighLevelClient.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String[] urlList = clusterNodes.split(",");
        HttpHost[] nodes = new HttpHost[urlList.length];
        for (int i = 0; i < urlList.length; i++) {
            URL url = new URL(urlList[i]);
            HttpHost node = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
            nodes[i] = node;
        }
        RestClientBuilder builder = RestClient.builder(nodes);
        restHighLevelClient = new RestHighLevelClient(builder);
    }

}
