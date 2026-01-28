package com.SoufianeDev.TaskApp.Service.Impl;

import com.SoufianeDev.TaskApp.Service.TaskService;
import com.SoufianeDev.TaskApp.domain.CreateTaskRequest;
import com.SoufianeDev.TaskApp.domain.UpdateTaskRequest;
import com.SoufianeDev.TaskApp.domain.entity.Task;
import com.SoufianeDev.TaskApp.domain.entity.TaskStatus;
import com.SoufianeDev.TaskApp.exception.TaskNotFoundException;
import com.SoufianeDev.TaskApp.repository.TaskRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    @Override
    public Task createTask(CreateTaskRequest request) {
        Instant now = Instant.now();
        Task newTask = new Task(
                null,
                request.title(),
                request.description(),
                request.dueDate(),
                TaskStatus.OPEN,
                request.priority(),
                now,
                now
        );
        return taskRepository.save(newTask);
    }

    @Override
    public List<Task> listTasks() {
        return taskRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Task updateTask(UUID taskId, UpdateTaskRequest request) {
        // FIX: Add proper error handling
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));  // <-- FIXED!

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setPriority(request.priority());
        task.setStatus(request.status());
        task.setUpdated(Instant.now());

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(UUID taskId) {
        taskRepository.deleteById(taskId);
    }
}
