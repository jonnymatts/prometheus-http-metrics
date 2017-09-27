package com.jonnymatts.prometheus.http.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ConfigurationObjectMapper extends ObjectMapper {
    public ConfigurationObjectMapper() {
        super(new YAMLFactory());
        registerModule(new JavaTimeModule());
        registerModule(new Jdk8Module());
    }
}