package ostro.veda.product_ms.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "controller.defaults")
public class ControllerDefaults {

    public static final String MAPPING_PREFIX = "/api";
    public static final String MAPPING_VERSION_SUFFIX = "/v1";
}
