package com.SoufianeDev.TaskApp.domain;

import com.SoufianeDev.TaskApp.domain.entity.TaskPriority;
import com.SoufianeDev.TaskApp.domain.entity.TaskStatus;

import java.time.LocalDate;

public record UpdateTaskRequest(
        String title,
        String description,
        LocalDate dueDate,
        TaskStatus status,
        TaskPriority priority
) {
}
