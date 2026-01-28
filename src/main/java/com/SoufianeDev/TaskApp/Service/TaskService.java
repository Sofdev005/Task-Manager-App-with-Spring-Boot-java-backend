package com.SoufianeDev.TaskApp.Service;

import com.SoufianeDev.TaskApp.domain.CreateTaskRequest;
import com.SoufianeDev.TaskApp.domain.UpdateTaskRequest;
import com.SoufianeDev.TaskApp.domain.entity.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    Task createTask(CreateTaskRequest request);
    List<Task> listTasks();
    Task updateTask(UUID id, UpdateTaskRequest request);
    void deleteTask(UUID taskId);
}
