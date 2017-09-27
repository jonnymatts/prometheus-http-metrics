package com.jonnymatts.prometheus.http.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonnymatts.prometheus.configuration.HistogramConfiguration;

public class HistogramConfigurationParser {
    private final ObjectMapper objectMapper;

    public HistogramConfigurationParser() {
        this.objectMapper = new ConfigurationObjectMapper();
    }

    public HistogramConfigurationParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public HistogramConfiguration parse(String configBody) {
        try {
            return objectMapper.readValue(configBody, HistogramConfiguration.class);
        } catch (Exception e) {
            throw new ConfigurationParsingException(configBody, e);
        }
    }
}