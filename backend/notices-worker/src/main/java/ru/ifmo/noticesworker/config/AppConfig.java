package ru.ifmo.noticesworker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.web.client.RestTemplate;
import ru.ifmo.common.dto.NoticeDTO;
import ru.ifmo.noticesworker.send.CustomNoticeSender;
import ru.ifmo.noticesworker.send.NoticeSender;
import ru.ifmo.noticesworker.send.RequestDirector;

@Configuration
public class AppConfig {
  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Value("${spring.kafka.consumer.group-id}")
  private String groupId;

  @Bean
  public ObjectMapper objectMapper() {
    return JacksonUtils.enhancedObjectMapper();
  }

  @Bean
  public ConsumerFactory<String, NoticeDTO> consumerFactory(ObjectMapper objectMapper) {
    var props = new HashMap<String, Object>();

    JsonDeserializer<NoticeDTO> deserializer = new JsonDeserializer<>(objectMapper);

    deserializer.addTrustedPackages("*");
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

    var kafkaProducerFactory = new DefaultKafkaConsumerFactory<String, NoticeDTO>(props);
    kafkaProducerFactory.setValueDeserializer(deserializer);
    return kafkaProducerFactory;
  }

  @Bean
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, NoticeDTO>>
      kafkaListenerContainerFactory(ObjectMapper objectMapper) {
    ConcurrentKafkaListenerContainerFactory<String, NoticeDTO> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory(objectMapper));
    return factory;
  }

  @Bean
  public RequestDirector requestDirector(RestTemplate template) {
    return new RequestDirector(template);
  }

  @Bean
  public NoticeSender noticeSender(RequestDirector requestDirector) {
    return new CustomNoticeSender(requestDirector);
  }

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate template = new RestTemplate();
    template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

    return template;
  }
}
