package com.demo.daniel.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "local")
@Data
public class LocalProperties {

    private String path;
}
