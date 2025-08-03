package com.example.project.web;

import com.example.project.dto.SearchResponseDto;
import com.example.project.service.CourseSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseSearchController {

    private final CourseSearchService courseSearchService;

    @GetMapping("/search")
    public ResponseEntity<SearchResponseDto> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false, defaultValue = "upcoming") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        SearchResponseDto response = courseSearchService.searchCourses(
                q, category, type, minAge, maxAge, minPrice, maxPrice, startDate, sort, page, size
        );
        return ResponseEntity.ok(response);
    }
}