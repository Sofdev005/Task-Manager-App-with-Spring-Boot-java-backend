package com.SoufianeDev.TaskApp.domain.dto;

import com.SoufianeDev.TaskApp.domain.entity.TaskPriority;
import com.SoufianeDev.TaskApp.domain.entity.TaskStatus;

import java.time.LocalDate;
import java.util.UUID;

public record TaskDto(
        UUID id,
        String title,
        String description,
        LocalDate dueDate,
        TaskPriority priority,
        TaskStatus status
) {
}