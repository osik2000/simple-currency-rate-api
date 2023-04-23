package pl.pawelosinski.dynatrace.nbp.task.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.pawelosinski.dynatrace.nbp.task.backend.service.CurrencyService;

@Configuration
@ComponentScan(basePackageClasses = CurrencyService.class)
public class CurrencyServiceConfig {

    @Bean
    @Profile("!integration")
    public String getApiCoreUrl(){
        return "http://api.nbp.pl";
    }

    @Bean
    @Profile("integration")
    public String testApiCoreUrl(){
        return "http://localhost:8123";
    }



}
