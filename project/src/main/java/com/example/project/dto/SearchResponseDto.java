package com.example.project.dto;

import java.util.List;

public record SearchResponseDto(
        long total,
        List<CourseSummaryDto> courses
) {}
