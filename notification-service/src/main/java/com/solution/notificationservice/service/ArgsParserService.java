package com.solution.notificationservice.service;

import com.solution.notificationservice.dto.ReportArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArgsParserService {

    public ReportArgs parseReportArgs(String[] args) {
        List<String> serviceNames = null;
        Integer periodHours = null;

        switch (args.length) {
            case 0 -> {/* empty */}
            case 1 -> {
                String arg = args[0];
                if (isNumber(arg)) {
                    periodHours = Integer.parseInt(arg);
                } else if (arg != null && arg.contains(",")) {
                    serviceNames = Arrays.asList(arg.split(","));
                } else {
                    serviceNames = List.of(arg);
                }
            }
            case 2 -> {
                String first = args[0];
                String second = args[1];

                if (isNumber(first) && isNumber(second)) {
                    periodHours = Math.min(Integer.parseInt(first), 48);
                } else if (isNumber(first)) {
                    periodHours = Math.min(Integer.parseInt(first), 48);
                    serviceNames = parseServiceNames(second);
                } else if (isNumber(second)) {
                    serviceNames = parseServiceNames(first);
                    periodHours = Math.min(Integer.parseInt(second), 48);
                } else {
                    serviceNames = parseServiceNames(first);
                    periodHours = 24;
                }
            }
            default -> throw new IllegalArgumentException("Too many arguments: " + args.length);
        }

        return new ReportArgs(periodHours, serviceNames);
    }

    private List<String> parseServiceNames(String arg) {
        if (arg == null || arg.isEmpty()) {
            return List.of();
        }
        return arg.contains(",")
                ? Arrays.asList(arg.split(","))
                : List.of(arg);
    }

    private boolean isNumber(String s) {
        return s != null && s.matches("\\d+");
    }
}
