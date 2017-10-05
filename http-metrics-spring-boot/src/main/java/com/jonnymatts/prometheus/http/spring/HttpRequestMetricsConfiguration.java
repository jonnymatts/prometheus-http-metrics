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

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;

import static java.lang.String.format;
import static java.nio.charset.Charset.defaultCharset;

@Configuration
public class HttpRequestMetricsConfiguration {

    static final String DEFAULT_CONFIG_FILE_PATH = "/config/http-request-metric-config.yaml";

    private @Autowired HttpRequestMetricFilter httpRequestMetricFilter;

    private Environment environment;

    public HttpRequestMetricsConfiguration(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void postConstruct() {
        httpRequestMetricFilter.register();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(HttpRequestMetricFilter httpRequestMetricFilter) {
        return new FilterRegistrationBean(httpRequestMetricFilter);
    }

    @Bean
    public HttpRequestMetricFilter httpRequestMetricFilter(HistogramConfigurationParser histogramConfigurationParser) {
        final String configFileLocation = environment.getProperty("http.metrics.exporter.config.path", DEFAULT_CONFIG_FILE_PATH);
        final File file = new File(configFileLocation);
        final boolean fileDoesNotExist = !file.exists();
        if(fileDoesNotExist && configFileLocation.equals(DEFAULT_CONFIG_FILE_PATH))
            return new HttpRequestMetricFilter();
        if(fileDoesNotExist)
            throw new IllegalArgumentException(format("Error configuring HTTP metric exporter: file %s does not exist", file.getAbsolutePath()));
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
