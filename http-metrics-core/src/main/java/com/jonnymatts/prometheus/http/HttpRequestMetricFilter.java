package com.jonnymatts.prometheus.http;

import com.jonnymatts.prometheus.configuration.HistogramConfiguration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpRequestMetricFilter implements Filter {

    private final HttpRequestMetricCounter counter;
    private final HttpRequestDurationHistogram histogram;

    public HttpRequestMetricFilter(HttpRequestMetricCounter counter, HttpRequestDurationHistogram histogram) {
        this.counter = counter;
        this.histogram = histogram;
    }

    public HttpRequestMetricFilter(HistogramConfiguration histogramConfiguration) {
        this.counter = new HttpRequestMetricCounter();
        this.histogram = new HttpRequestDurationHistogram(histogramConfiguration);
    }

    public HttpRequestMetricFilter() {
        this.counter = new HttpRequestMetricCounter();
        this.histogram = new HttpRequestDurationHistogram();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest requestFacade = (HttpServletRequest) request;
        final HttpServletResponse responseFacade = (HttpServletResponse) response;
        final String path = requestFacade.getServletPath().substring(1);

        histogram.labels(path)
                .time(() -> {
                            try {
                                chain.doFilter(request, response);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                );

        counter.labels(
                requestFacade.getMethod().toLowerCase(),
                path,
                String.valueOf(responseFacade.getStatus())
        ).inc();
    }

    @Override
    public void destroy() {
    }

    public void register() {
        counter.register();
        histogram.register();
    }
}