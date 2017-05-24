package com.jonnymatts.prometheus.http.spring;

import com.jonnymatts.prometheus.http.HttpRequestMetricFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpRequestMetricsConfiguration {

    @Bean
    public FilterRegistrationBean httpMetricFilter() {
        return new FilterRegistrationBean(new HttpRequestMetricFilter());
    }
}
