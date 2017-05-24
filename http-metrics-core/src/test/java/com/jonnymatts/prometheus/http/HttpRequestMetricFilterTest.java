package com.jonnymatts.prometheus.http;

import io.prometheus.client.Counter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpRequestMetricFilterTest {

    @Mock private HttpRequestMetricCounter counter;
    @Mock private Counter.Child counterChild;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain chain;

    private HttpRequestMetricFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = new HttpRequestMetricFilter(counter);
    }

    @Test
    public void filterIncreasesCounterWithCorrectLabels() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getServletPath()).thenReturn("/path");
        when(response.getStatus()).thenReturn(200);
        when(counter.labels("get", "path", "200")).thenReturn(counterChild);

        filter.doFilter(request, response, chain);

        verify(counterChild).inc();
        verify(chain).doFilter(request, response);
    }
}