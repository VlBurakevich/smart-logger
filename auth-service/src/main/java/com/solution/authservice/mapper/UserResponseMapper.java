package com.solution.authservice.mapper;

import com.solution.authservice.dto.response.UserResponse;
import com.solution.authservice.entity.Role;
import com.solution.authservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "email", source = "credential.email")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    UserResponse toResponse(User user);

    @Named("mapRoles")
    default List<String> mapRoles(Set<Role> roles) {
        if (roles == null) return List.of();
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

}
