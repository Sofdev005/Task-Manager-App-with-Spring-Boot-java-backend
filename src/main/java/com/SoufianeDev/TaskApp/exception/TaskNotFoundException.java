package com.SoufianeDev.TaskApp.exception;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {
    private final UUID id;
    public TaskNotFoundException(UUID taskId) {
        super("Task with id " + taskId + " not found");
        this.id = taskId;
    }

    public UUID getId() {
        return id;
    }
}
