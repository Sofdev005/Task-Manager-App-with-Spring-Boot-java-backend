package com.SoufianeDev.TaskApp.domain;

import com.SoufianeDev.TaskApp.domain.entity.TaskPriority;
import com.SoufianeDev.TaskApp.domain.entity.Task;
import java.time.LocalDate;

public record CreateTaskRequest(
        String title,
        String description,
        LocalDate dueDate,
        TaskPriority priority
) {

}
