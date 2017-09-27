package com.jonnymatts.prometheus.http.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonnymatts.prometheus.configuration.HistogramConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HistogramConfigurationParserTest {

    private static final String CONFIG_BODY = "configBody";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock private ObjectMapper objectMapper;
    @Mock private HistogramConfiguration configuration;

    private HistogramConfigurationParser configurationParser;

    @Before
    public void setUp() throws Exception {
        when(objectMapper.readValue(CONFIG_BODY, HistogramConfiguration.class)).thenReturn(configuration);

        configurationParser = new HistogramConfigurationParser(objectMapper);
    }

    @Test
    public void parseReturnsCorrectConfig() throws Exception {
        final HistogramConfiguration got = configurationParser.parse(CONFIG_BODY);

        assertThat(got).isEqualTo(configuration);
    }

    @Test
    public void parseThrowsConfigurationDeserializationExceptionIfObjectMapperThrowsException() throws Exception {
        final String errorMessage = "error!";
        when(objectMapper.readValue(CONFIG_BODY, HistogramConfiguration.class)).thenThrow(new RuntimeException(errorMessage));

        expectedException.expect(ConfigurationParsingException.class);
        expectedException.expectCause(isA(RuntimeException.class));
        expectedException.expectMessage("Could not");
        expectedException.expectMessage(CONFIG_BODY);

        configurationParser.parse(CONFIG_BODY);
    }
}