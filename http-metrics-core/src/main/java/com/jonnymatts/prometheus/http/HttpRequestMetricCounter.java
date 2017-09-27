package com.jonnymatts.prometheus.http;

import com.jonnymatts.prometheus.collectors.PrometheusCounter;

public class HttpRequestMetricCounter extends PrometheusCounter {

    public HttpRequestMetricCounter() {
        super("http_requests_total",
                "Total HTTP requests handled",
                "method", "handler", "code");
    }
}