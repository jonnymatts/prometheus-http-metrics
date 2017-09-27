package com.jonnymatts.prometheus.http;

import com.jonnymatts.prometheus.configuration.HistogramConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.jonnymatts.prometheus.http.HttpRequestDurationHistogram.*;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpRequestDurationHistogramTest {

    @Mock private HistogramConfiguration configuration;

    @Before
    public void setUp() throws Exception {
        when(configuration.getName()).thenReturn("name");
        when(configuration.getDescription()).thenReturn("description");
        when(configuration.getLabels()).thenReturn(new String[]{"blah"});
        when(configuration.getBuckets()).thenReturn(singletonList(1d));
    }

    @Test
    public void constructorSetsNameToDefaultValueIfItIsNull() throws Exception {
        when(configuration.getName()).thenReturn(null);

        new HttpRequestDurationHistogram(configuration);

        verify(configuration).setName(DEFAULT_NAME);
    }

    @Test
    public void constructorSetsDescriptionToDefaultValueIfItIsNull() throws Exception {
        when(configuration.getDescription()).thenReturn(null);

        new HttpRequestDurationHistogram(configuration);

        verify(configuration).setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    public void constructorSetsLabelsToDefaultValueIfItIsNull() throws Exception {
        when(configuration.getLabels()).thenReturn(null);

        new HttpRequestDurationHistogram(configuration);

        verify(configuration).setLabels(DEFAULT_LABELS);
    }
}