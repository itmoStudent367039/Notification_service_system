package ru.ifmo.authapi.util;

import org.modelmapper.ModelMapper;

public class ObjectConverter {
  private final ModelMapper modelMapper;

  public ObjectConverter(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public <T, C> C convertToObject(T from, Class<C> to) {
    return this.modelMapper.map(from, to);
  }
}
