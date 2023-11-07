package ru.ifmo.backend.util;

import org.mapstruct.*;
import ru.ifmo.backend.dto.TelegramChatIdDTO;
import ru.ifmo.backend.dto.UpdateDTO;
import ru.ifmo.backend.dto.VkIdDTO;
import ru.ifmo.backend.models.Person;

@Mapper(componentModel = "spring")
public interface PeopleMapper {
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updatePersonFromUpdateDTO(UpdateDTO dto, @MappingTarget Person person);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updatePersonFromVkIdDTO(VkIdDTO idDTO, @MappingTarget Person person);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updatePersonFromTelegramChatIdDTO(TelegramChatIdDTO idDTO, @MappingTarget Person person);
}
