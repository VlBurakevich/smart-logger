package com.solution.coreservice.mapper;

import com.solution.coreservice.dto.response.ApiKeyInfoResponse;
import com.solution.coreservice.dto.response.ApiKeyResponse;
import com.solution.coreservice.entity.ApiKey;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApiKeyMapper {

    ApiKeyResponse toResponseWithKey(ApiKey entity, String rawApiKey);

    ApiKeyInfoResponse toInfoResponse(ApiKey apiKey);
}
