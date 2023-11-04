package ru.ifmo.notificationservice.Notificationservice.util;

import org.mapstruct.*;
import ru.ifmo.notificationservice.Notificationservice.dto.TelegramChatIdDTO;
import ru.ifmo.notificationservice.Notificationservice.dto.UpdateDTO;
import ru.ifmo.notificationservice.Notificationservice.dto.VkIdDTO;
import ru.ifmo.notificationservice.Notificationservice.models.Person;

@Mapper(componentModel = "spring")
public interface PeopleMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePersonFromUpdateDTO(UpdateDTO dto, @MappingTarget Person person);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePersonFromVkIdDTO(VkIdDTO idDTO, @MappingTarget Person person);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePersonFromTelegramChatIdDTO(TelegramChatIdDTO idDTO, @MappingTarget Person person);

}
