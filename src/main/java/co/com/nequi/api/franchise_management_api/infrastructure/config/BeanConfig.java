package co.com.nequi.api.franchise_management_api.infrastructure.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "co.com.nequi.api.franchise_management_api.domain",
        "co.com.nequi.api.franchise_management_api.application",
        "co.com.nequi.api.franchise_management_api.infrastructure"
})
public class BeanConfig {
}
