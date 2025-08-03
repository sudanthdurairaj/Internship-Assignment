package com.example.project.service;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData; // <-- MAKE SURE THIS IMPORT IS HERE
import com.example.project.dto.CourseSummaryDto;
import com.example.project.dto.SearchResponseDto;
import com.example.project.model.CourseDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public SearchResponseDto searchCourses(
            String q, String category, String type,
            Integer minAge, Integer maxAge, Double minPrice, Double maxPrice,
            LocalDate startDate, String sort, int page, int size
    ) {
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        List<Query> filters = new ArrayList<>();

        // Full-text search
        if (StringUtils.hasText(q)) {
            boolQueryBuilder.must(new Query.Builder()
                    .multiMatch(m -> m
                            .query(q)
                            .fields("title", "description")
                    ).build());
        }

        // Exact filters
        if (StringUtils.hasText(category)) {
            filters.add(new Query.Builder().term(t -> t.field("category").value(category)).build());
        }
        if (StringUtils.hasText(type)) {
            filters.add(new Query.Builder().term(t -> t.field("type").value(type)).build());
        }

        // Range filters with the fix
        if (minAge != null) {
            filters.add(new Query.Builder().range(r -> r.field("maxAge").gte(JsonData.of(minAge))).build());
        }
        if (maxAge != null) {
            filters.add(new Query.Builder().range(r -> r.field("minAge").lte(JsonData.of(maxAge))).build());
        }
        if (minPrice != null) {
            filters.add(new Query.Builder().range(r -> r.field("price").gte(JsonData.of(minPrice))).build());
        }
        if (maxPrice != null) {
            filters.add(new Query.Builder().range(r -> r.field("price").lte(JsonData.of(maxPrice))).build());
        }
        if (startDate != null) {
            String isoDateTime = startDate.atStartOfDay().atOffset(ZoneOffset.UTC).toString();
            filters.add(new Query.Builder().range(r -> r.field("nextSessionDate").gte(JsonData.of(isoDateTime))).build());
        }

        boolQueryBuilder.filter(filters);
        Query finalQuery = new Query.Builder().bool(boolQueryBuilder.build()).build();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(finalQuery)
                .withPageable(PageRequest.of(page, size))
                .withSort(buildSortOptions(sort))
                .build();

        SearchHits<CourseDocument> searchHits = elasticsearchOperations.search(nativeQuery, CourseDocument.class);

        List<CourseSummaryDto> courseSummaries = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(doc -> new CourseSummaryDto(
                        doc.getId(),
                        doc.getTitle(),
                        doc.getCategory(),
                        doc.getPrice(),
                        doc.getNextSessionDate()
                ))
                .collect(Collectors.toList());

        return new SearchResponseDto(searchHits.getTotalHits(), courseSummaries);
    }

    private List<SortOptions> buildSortOptions(String sort) {
        String sortField;
        SortOrder sortOrder;

        switch (Objects.requireNonNullElse(sort, "upcoming")) {
            case "priceAsc":
                sortField = "price";
                sortOrder = SortOrder.Asc;
                break;
            case "priceDesc":
                sortField = "price";
                sortOrder = SortOrder.Desc;
                break;
            case "upcoming":
            default:
                sortField = "nextSessionDate";
                sortOrder = SortOrder.Asc;
                break;
        }

        return List.of(new SortOptions.Builder().field(f -> f.field(sortField).order(sortOrder)).build());
    }
}