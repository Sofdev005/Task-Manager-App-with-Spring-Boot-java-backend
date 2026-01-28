package com.SoufianeDev.TaskApp.mapper.impl;

import com.SoufianeDev.TaskApp.domain.CreateTaskRequest;
import com.SoufianeDev.TaskApp.domain.UpdateTaskRequest;
import com.SoufianeDev.TaskApp.domain.dto.CreateTaskRequestDto;
import com.SoufianeDev.TaskApp.domain.dto.TaskDto;
import com.SoufianeDev.TaskApp.domain.dto.UpdateTaskRequestDto;
import com.SoufianeDev.TaskApp.domain.entity.Task;
import com.SoufianeDev.TaskApp.mapper.TaskMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements TaskMapper {
    @Override
    public CreateTaskRequest fromDto(CreateTaskRequestDto dto) {
        return new CreateTaskRequest(
                dto.title(), dto.description(), dto.dueDate(),dto.priority()
        );
    }

    @Override
    public TaskDto toDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(), task.getDueDate(),task.getPriority(),task.getStatus()
        );
    }

    @Override
    public UpdateTaskRequest fromDto(UpdateTaskRequestDto dto) {
        return new UpdateTaskRequest(
                dto.title(), dto.description(), dto.dueDate(),dto.status(),dto.priority()
        );
    }
}
