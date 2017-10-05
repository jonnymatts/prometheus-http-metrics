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
        super(configuration);
    }

    public HttpRequestDurationHistogram() {
        super(DEFAULT_NAME,
                DEFAULT_DESCRIPTION,
                DEFAULT_LABELS);
    }
}