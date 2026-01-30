package com.solution.coreservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceConfigRequest {

    private String serviceName;

    private Integer checkMin;

    private Integer reportHr;
}
