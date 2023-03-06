package com.api.wordgames.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigModelMapper {

    @Bean
    public org.modelmapper.ModelMapper modelMapper(){
        return new org.modelmapper.ModelMapper();
    }
}
