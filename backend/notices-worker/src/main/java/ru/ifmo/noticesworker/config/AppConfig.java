package ru.ifmo.noticesworker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.ifmo.noticesworker.model.Notice;

@Configuration
public class AppConfig {
  @Bean
  public ObjectMapper objectMapper() {
    return JacksonUtils.enhancedObjectMapper();
  }

  @Bean
  public ProducerFactory<String, Notice> producerFactory(ObjectMapper mapper) {
    var configProps = new HashMap<String, Object>();
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, Notice>(configProps);
    kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(mapper));

    return kafkaProducerFactory;
  }
}
