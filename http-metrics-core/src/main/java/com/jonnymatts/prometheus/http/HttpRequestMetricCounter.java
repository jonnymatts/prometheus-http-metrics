package com.jonnymatts.prometheus.http;

import io.prometheus.client.Collector;
import io.prometheus.client.Counter;
import io.prometheus.client.Counter.Child;

import java.util.List;

class HttpRequestMetricCounter {
    private final Counter counter;

    public HttpRequestMetricCounter() {
        this.counter = Counter.build()
                .name("http_requests_total")
                .help("Total HTTP requests handled")
                .labelNames("method", "handler", "code")
                .create()
                .register();
    }

    public Child labels(String... labels) {
        return counter.labels(labels);
    }

    public void inc() {
        inc(0);
    }

    public void inc(double amount) {
        counter.inc(amount);
    }

    public List<Collector.MetricFamilySamples> collect(double amount) {
        return counter.collect();
    }

    public List<Collector.MetricFamilySamples> describe(double amount) {
        return counter.describe();
    }

    public double get() {
        return counter.get();
    }
}