package com.example.project.config;

import com.example.project.model.CourseDocument;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.List;

@Component
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ObjectMapper objectMapper;

    @Autowired
    public DataInitializer(ElasticsearchOperations elasticsearchOperations, ObjectMapper objectMapper) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // This is the main change: Get the IndexOperations by binding to the class
        IndexOperations indexOps = elasticsearchOperations.indexOps(CourseDocument.class);

        if (indexOps.exists()) {
            log.info("Index 'courses' already exists. Skipping data initialization.");
            return;
        }

        log.info("Creating index 'courses' with mapping and ingesting data.");
        indexOps.createWithMapping(); // This will now work correctly

        try (InputStream inputStream = new ClassPathResource("sample-courses.json").getInputStream()) {
            List<CourseDocument> courses = objectMapper.readValue(inputStream, new TypeReference<>() {});
            elasticsearchOperations.save(courses);
            log.info("Successfully indexed {} course documents.", courses.size());
        } catch (Exception e) {
            log.error("Failed to read or index sample data.", e);
        }
    }
}