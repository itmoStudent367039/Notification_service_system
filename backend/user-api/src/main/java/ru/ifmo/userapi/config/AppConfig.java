package ru.ifmo.userapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.client.RestTemplate;
import ru.ifmo.common.dto.NoticeDTO;
import ru.ifmo.userapi.services.DataSender;
import ru.ifmo.userapi.services.DataSenderKafka;
import ru.ifmo.userapi.services.NoticesService;

@Configuration
@Slf4j
public class AppConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Value("${application.kafka.topic-name}")
  private String topicName;

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

  @Bean
  public ObjectMapper objectMapper() {
    return JacksonUtils.enhancedObjectMapper();
  }

  @Bean
  public ProducerFactory<String, NoticeDTO> producerFactory(ObjectMapper mapper) {
    var configProps = new HashMap<String, Object>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, NoticeDTO>(configProps);
    kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(mapper));
    return kafkaProducerFactory;
  }

  @Bean
  public KafkaTemplate<String, NoticeDTO> kafkaTemplate(
      ProducerFactory<String, NoticeDTO> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }

  @Bean
  public NewTopic topic() {
    return TopicBuilder.name(topicName).partitions(1).replicas(1).build();
  }

  @Bean
  public DataSender dataSender(
      NewTopic topic,
      KafkaTemplate<String, NoticeDTO> kafkaTemplate,
      NoticesService noticesService) {
    return new DataSenderKafka(kafkaTemplate, noticesService, topic.name());
  }
}
