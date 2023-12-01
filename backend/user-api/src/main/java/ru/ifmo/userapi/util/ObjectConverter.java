package ru.ifmo.userapi.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectConverter {
  private final ModelMapper modelMapper;

  @Autowired
  public ObjectConverter(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public <T, C> C convertToObject(T from, Class<C> to) {
    return this.modelMapper.map(from, to);
  }
}
