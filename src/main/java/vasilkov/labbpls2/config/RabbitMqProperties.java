package vasilkov.labbpls2.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@EnableConfigurationProperties
@ConfigurationProperties(prefix = "rabbit-mq")
@Configuration
@Slf4j
@Data
public class RabbitMqProperties {

    private String host;
    private String user;
    private String password;
    private String virtualHost;
    private String queue;
    private String exchange;
    private String routingKey;

    @PostConstruct
    private void initParam(){
        log.info("host: {},  user: {}, password: {}, virtualHost: {}", host,  user, password, virtualHost);
    }
}