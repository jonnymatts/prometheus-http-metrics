package com.jonnymatts.prometheus.http.spring;

import com.jonnymatts.prometheus.configuration.HistogramConfiguration;
import com.jonnymatts.prometheus.http.HttpRequestMetricFilter;
import com.jonnymatts.prometheus.http.configuration.HistogramConfigurationParser;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.FileInputStream;

import static java.nio.charset.Charset.defaultCharset;

@Configuration
public class HttpRequestMetricsConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public FilterRegistrationBean httpMetricFilter(HttpRequestMetricFilter httpRequestMetricFilter) {
        return new FilterRegistrationBean(httpRequestMetricFilter);
    }

    @Bean
    public HttpRequestMetricFilter httpRequestMetricFilter(HistogramConfigurationParser histogramConfigurationParser) {
        final String configFileLocation = environment.getProperty("http.metrics.exporter.config.path", "/config/config.yaml");
        final File file = new File(configFileLocation);
        if(!file.exists())
            return new HttpRequestMetricFilter();
        return new HttpRequestMetricFilter(parseFile(histogramConfigurationParser, file));
    }

    @Bean
    public HistogramConfigurationParser histogramConfigurationParser() {
        return new HistogramConfigurationParser();
    }

    private HistogramConfiguration parseFile(HistogramConfigurationParser histogramConfigurationParser, File file) {
        try {
            final String configBody = IOUtils.toString(new FileInputStream(file), defaultCharset());
            return histogramConfigurationParser.parse(configBody);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
