package com.solution.coreservice.mapper;

import com.solution.coreservice.dto.response.ReportResponse;
import com.solution.coreservice.dto.response.ReportShortResponse;
import com.solution.coreservice.entity.Report;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    ReportResponse toResponse(Report report);

    ReportShortResponse toShortResponse(Report report);
}
