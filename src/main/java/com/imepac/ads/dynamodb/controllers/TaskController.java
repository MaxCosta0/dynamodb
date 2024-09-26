package com.imepac.ads.dynamodb.controllers;

import com.imepac.ads.dynamodb.entities.Tasks;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @GetMapping("/{userId}/{taskId}")
//    public ResponseEntity<Task> getTask(@PathVariable String userId, @PathVariable String taskId) {
//        Task task = dynamoDbTemplate.ge(userId, taskId);
//        if (task == null) {
//            return ResponseEntity.status(404).body(null);
//        }
//        return ResponseEntity.ok(task);
//    }
}
