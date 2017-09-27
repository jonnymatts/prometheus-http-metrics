package com.jonnymatts.prometheus.http.spring;

import com.jonnymatts.prometheus.configuration.HistogramConfiguration;
import com.jonnymatts.prometheus.http.configuration.HistogramConfigurationParser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import static com.jonnymatts.prometheus.http.spring.HttpRequestMetricsConfiguration.DEFAULT_CONFIG_FILE_PATH;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpRequestMetricsConfigurationTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();
    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock private Environment environment;
    @Mock private HistogramConfigurationParser parser;

    private HistogramConfiguration histogramConfiguration;

    private HttpRequestMetricsConfiguration configuration;

    @Before
    public void setUp() throws Exception {
        when(environment.getProperty(eq("http.metrics.exporter.config.path"), any(String.class))).thenReturn(DEFAULT_CONFIG_FILE_PATH);
        histogramConfiguration = new HistogramConfiguration(singletonList(1d), null, null, "name", "description");

        configuration = new HttpRequestMetricsConfiguration(environment);
    }

    @Test
    public void httpRequestMetricFilterUsesDefaultHistogramIfConfigFilePropertyIsNotSet() throws Exception {
        configuration.httpRequestMetricFilter(parser);

        verifyZeroInteractions(parser);
    }

    @Test
    public void httpRequestMetricFilterUsesCustomHistogramIfConfigFilePropertyIsSet() throws Exception {
        final File configFile = temporaryFolder.newFile("config.yaml");
        try(final Writer writer = new FileWriter(configFile)) {
            writer.write("blah");
        }

        when(environment.getProperty(eq("http.metrics.exporter.config.path"), any(String.class))).thenReturn(configFile.getAbsolutePath());
        when(parser.parse("blah")).thenReturn(histogramConfiguration);

        configuration.httpRequestMetricFilter(parser);
    }

    @Test
    public void httpRequestMetricFilterThrowsExceptionIfConfigFilePropertyIsSetButFileDoesNotExist() throws Exception {
        when(environment.getProperty(eq("http.metrics.exporter.config.path"), any(String.class))).thenReturn("/randomFile.yaml");

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Error configuring HTTP metric exporter: file /randomFile.yaml does not exist");

        configuration.httpRequestMetricFilter(parser);
    }
}