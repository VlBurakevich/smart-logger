package com.solution.coreservice.mapper;

import com.solution.coreservice.dto.response.ReportResponse;
import com.solution.coreservice.dto.response.ReportShortResponse;
import com.solution.coreservice.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReportMapper {

    ReportResponse toResponse(Report report);

    ReportShortResponse toShortResponse(Report report);
}
