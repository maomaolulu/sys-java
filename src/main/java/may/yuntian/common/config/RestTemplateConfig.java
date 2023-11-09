package may.yuntian.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置类
 * @author: liyongqiang
 * @create: 2023-03-25 15:06
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
