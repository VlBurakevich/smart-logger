package com.solution.coreservice.mapper;

import com.solution.coreservice.dto.response.ApiKeyResponse;
import com.solution.coreservice.entity.ApiKey;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApiKeyMapper {
    ApiKeyResponse toResponse(ApiKey apiKey);
}
