package ostro.veda.product_ms.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "controller.defaults")
public class ControllerDefaults {

    public static String mappingPrefix;
    public static String currentVersionSuffix;
}
