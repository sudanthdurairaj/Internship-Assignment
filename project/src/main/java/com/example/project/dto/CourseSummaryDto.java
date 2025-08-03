package com.example.project.dto;
import java.time.OffsetDateTime;

public record CourseSummaryDto(
        String id,
        String title,
        String category,
        double price,
        OffsetDateTime nextSessionDate
) {}
