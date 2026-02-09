package com.solution.coreservice.mapper;

import com.solution.coreservice.dto.response.ApiKeyInfoResponse;
import com.solution.coreservice.dto.response.ApiKeyResponse;
import com.solution.coreservice.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ApiKeyMapper {

    ApiKeyResponse toResponseWithKey(ApiKey entity, String rawApiKey);

    ApiKeyInfoResponse toInfoResponse(ApiKey apiKey);
}
