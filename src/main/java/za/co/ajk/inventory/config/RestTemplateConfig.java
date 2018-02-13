package za.co.ajk.inventory.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {
    
    @Bean
    @LoadBalanced
    @RefreshScope
    public RestTemplate restTemplate(RestTemplateBuilder builder, ApplicationProperties props) {
        builder.setConnectTimeout(props.getRestTemplateConfig().getConnectTimeout());
        builder.setReadTimeout(props.getRestTemplateConfig().getReadTimeout());
        return builder.build();
    }
}
