package com.imepac.ads.dynamodb.repositories;

import com.imepac.ads.dynamodb.entities.Tasks;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.UUID;

@Repository
public class TaskRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<Tasks> taskTable;

    public TaskRepository(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
        this.taskTable = enhancedClient.table("Tasks", TableSchema.fromBean(Tasks.class));
    }

    public void saveTask(Tasks tasks) {
        if (tasks.getUserId() == null || tasks.getUserId().isEmpty()) {
            throw new IllegalArgumentException("UserId is required");
        }

        if (tasks.getTaskId() == null || tasks.getTaskId().isEmpty()) {
            tasks.setTaskId(UUID.randomUUID().toString());
        }

        taskTable.putItem(tasks);
    }

    public Tasks getTask(String userId, String taskId) {
        if (userId == null || userId.isEmpty() || taskId == null || taskId.isEmpty()) {
            throw new IllegalArgumentException("UserId and TaskId are required");
        }

        return taskTable.getItem(r -> r.key(k -> k.partitionValue(userId).sortValue(taskId)));
    }
}
