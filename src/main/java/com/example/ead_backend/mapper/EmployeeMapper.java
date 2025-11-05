package com.example.ead_backend.mapper;

import org.mapstruct.*;

import com.example.ead_backend.dto.EmployeeCreateDTO;
import com.example.ead_backend.model.entity.Employee;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {

    // Map Employee Entity to DTO
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "joinedDate", target = "joinedDate")
    EmployeeCreateDTO toDTO(Employee employee);

    // Update existing Employee from DTO
    @Mapping(source = "firstName", target = "user.firstName")
    @Mapping(source = "lastName", target = "user.lastName")
    @Mapping(source = "email", target = "user.email")
    @Mapping(target = "joinedDate", ignore = true) 
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "projects", ignore = true)
    void updateEntityFromDTO(EmployeeCreateDTO dto, @MappingTarget Employee employee);
}
