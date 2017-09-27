package com.jonnymatts.prometheus.http;

import com.jonnymatts.prometheus.collectors.PrometheusHistogram;
import com.jonnymatts.prometheus.configuration.HistogramConfiguration;
import io.prometheus.client.Histogram;

public class HttpRequestDurationHistogram extends PrometheusHistogram {

    public static final String DEFAULT_NAME = "http_request_duration_seconds";
    public static final String DEFAULT_DESCRIPTION = "HTTP request durations in seconds";
    public static final String[] DEFAULT_LABELS = new String[]{"handler"};

    public HttpRequestDurationHistogram(Histogram.Builder builder, HistogramConfiguration configuration) {
        super(builder, configuration);
    }

    public HttpRequestDurationHistogram(HistogramConfiguration configuration) {
        super(validate(configuration));
    }

    private static HistogramConfiguration validate(HistogramConfiguration configuration) {
        if(configuration.getName() == null)
            configuration.setName(DEFAULT_NAME);
        if(configuration.getDescription() == null)
            configuration.setDescription(DEFAULT_DESCRIPTION);
        if(configuration.getLabels() == null)
            configuration.setLabels(DEFAULT_LABELS);
        return configuration;
    }

    public HttpRequestDurationHistogram() {
        super(DEFAULT_NAME,
                DEFAULT_DESCRIPTION,
                DEFAULT_LABELS);
    }
}