package ru.ifmo.authapi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import ru.ifmo.authapi.util.ObjectConverter;
import ru.ifmo.authapi.util.validators.BindingChecker;

@Configuration
public class AppConfig {
  @Bean
  public BindingChecker bindingChecker() {
    return new BindingChecker();
  }

  @Bean
  public ObjectConverter objectConverter() {
    return new ObjectConverter(modelMapper());
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate template = new RestTemplate();
    template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

    return template;
  }
}
