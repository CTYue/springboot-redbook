package com.chuwa.redbook.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author b1go
 * @date 6/27/22 6:48 PM
 */
@Configuration
public class CommonConfig {

    /**
     * 当需要把第三方lib放入到IOC容器时候，则会用@Bean
     * @return
     */
    // "modelmapper" -> new ModelMapper();
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
