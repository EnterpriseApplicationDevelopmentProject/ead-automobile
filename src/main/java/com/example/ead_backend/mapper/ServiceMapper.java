package com.example.ead_backend.mapper;

import com.example.ead_backend.dto.ServiceDTO;
import com.example.ead_backend.model.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    ServiceDTO toDTO(Service service);

    @Mapping(target = "imagePublicId", ignore = true)
    Service toEntity(ServiceDTO dto);
}
