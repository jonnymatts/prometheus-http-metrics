package com.jonnymatts.prometheus.http;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpRequestMetricFilter implements Filter {

    private final HttpRequestMetricCounter counter;

    public HttpRequestMetricFilter(HttpRequestMetricCounter counter) {
        this.counter = counter;
    }

    public HttpRequestMetricFilter() {
        this.counter = new HttpRequestMetricCounter();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);

        final HttpServletRequest requestFacade = (HttpServletRequest) request;
        final HttpServletResponse responseFacade = (HttpServletResponse) response;

        counter.labels(
                requestFacade.getMethod().toLowerCase(),
                requestFacade.getServletPath().substring(1),
                String.valueOf(responseFacade.getStatus())
        ).inc();
    }

    @Override
    public void destroy() {
    }

}