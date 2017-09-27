package com.jonnymatts.prometheus.http.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jonnymatts.prometheus.configuration.ExponentialBucketConfiguration;
import com.jonnymatts.prometheus.configuration.HistogramConfiguration;
import com.jonnymatts.prometheus.configuration.LinearBucketConfiguration;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationObjectMapperTest {

    private static final HistogramConfiguration configuration = new HistogramConfiguration(
            asList(0.001d, 0.01d, 0.1d, 1d),
            new ExponentialBucketConfiguration(0.001, 10, 5),
            new LinearBucketConfiguration(0.001, 10, 5),
            "histogram1",
            "description",
            "label1", "label2"
    );

    private ObjectMapper objectMapper;
    private String configBody;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper(new YAMLFactory()).registerModule(new JavaTimeModule()).registerModule(new Jdk8Module());
        configBody = IOUtils.toString(getClass().getResourceAsStream("/test-config.yaml"), defaultCharset());
    }

    @Test
    public void objectMapperDeserializesConfigurationCorrectly() throws Exception {
        final HistogramConfiguration got = objectMapper.readValue(configBody, HistogramConfiguration.class);

        assertThat(got).isEqualTo(configuration);
    }
}