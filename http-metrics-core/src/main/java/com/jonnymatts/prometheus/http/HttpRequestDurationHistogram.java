package com.jonnymatts.prometheus.http;

import io.prometheus.client.Collector;
import io.prometheus.client.Histogram;

import java.util.List;

class HttpRequestDurationHistogram {
    private final Histogram histogram;

    public HttpRequestDurationHistogram() {
        this.histogram = Histogram.build()
                .name("http_request_duration_milliseconds")
                .help("HTTP request durations in milliseconds")
                .labelNames("handler")
                .create()
                .register();
    }

    public Histogram.Child labels(String... labels) {
        return histogram.labels(labels);
    }

    public void startTimer() {
        histogram.startTimer();
    }

    public void time(Runnable runnable) {
        histogram.time(runnable);
    }

    public List<Collector.MetricFamilySamples> collect(double amount) {
        return histogram.collect();
    }

    public List<Collector.MetricFamilySamples> describe(double amount) {
        return histogram.describe();
    }

    public void observe(double amount) {
        histogram.observe(amount);
    }
}