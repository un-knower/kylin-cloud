package com.kylin.user.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;

@Configuration
@ConditionalOnClass({HttpClient.class})
@EnableConfigurationProperties(HttpClientProperties.class)
public class HttpClientAutoConfiguration {

	private final HttpClientProperties properties;
	
	public HttpClientAutoConfiguration(HttpClientProperties properties){
		this.properties = properties;
	}
	
	@Autowired
	private LogbookHttpRequestInterceptor logbookHttpRequestInterceptor;
	
	@Autowired
	private LogbookHttpResponseInterceptor logbookHttpResponseInterceptor;
	
	/**
	 * httpclient bean 的定义
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(HttpClient.class)
	public HttpClient httpClient() {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(properties.getConnectTimeOut())
				// 构建requestConfig
				.setSocketTimeout(properties.getSocketTimeOut()).build();
		HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig)
				.setUserAgent(properties.getAgent())
				//最大链接数
				.setMaxConnPerRoute(properties.getMaxConnPerRoute())
				//总连接数
				.setMaxConnTotal(properties.getMaxConnTotaol())
				//这样http调用就会打印日志
				.addInterceptorFirst(logbookHttpRequestInterceptor)
				.addInterceptorFirst(logbookHttpResponseInterceptor)
				.build();
		return client;
	}
}
