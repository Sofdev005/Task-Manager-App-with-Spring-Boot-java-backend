package com.SoufianeDev.TaskApp.mapper;

import com.SoufianeDev.TaskApp.domain.CreateTaskRequest;
import com.SoufianeDev.TaskApp.domain.UpdateTaskRequest;
import com.SoufianeDev.TaskApp.domain.dto.CreateTaskRequestDto;
import com.SoufianeDev.TaskApp.domain.dto.TaskDto;
import com.SoufianeDev.TaskApp.domain.dto.UpdateTaskRequestDto;
import com.SoufianeDev.TaskApp.domain.entity.Task;

public interface TaskMapper {
    CreateTaskRequest fromDto(CreateTaskRequestDto dto);
    TaskDto toDto(Task task);
    UpdateTaskRequest fromDto(UpdateTaskRequestDto dto);
}
