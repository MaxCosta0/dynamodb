package com.imepac.ads.dynamodb.controllers;

import com.imepac.ads.dynamodb.entities.Tasks;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final DynamoDbTemplate dynamoDbTemplate;

    public TaskController(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody Tasks tasks) {
        tasks.setTaskId(UUID.randomUUID().toString());
        dynamoDbTemplate.save(tasks);
        return ResponseEntity.status(201).body("Task created successfully with ID: " + tasks.getTaskId());
    }

    @GetMapping("/{userId}/{taskId}")
    public ResponseEntity<List<Tasks>> getTask(@PathVariable String userId, @PathVariable String taskId) {

        var key = Key.builder()
                .partitionValue(userId)
                .sortValue(taskId)
                .build();

        var condition = QueryConditional.sortBeginsWith(key);

        var query = QueryEnhancedRequest.builder()
                .queryConditional(condition)
                .build();

        try {
            var task = dynamoDbTemplate.query(query, Tasks.class);

            List<Tasks> tasks = task.items().stream().toList();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }
}
