package com.SoufianeDev.TaskApp.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.SoufianeDev.TaskApp.Service.TaskService;
import com.SoufianeDev.TaskApp.domain.CreateTaskRequest;
import com.SoufianeDev.TaskApp.domain.UpdateTaskRequest;
import com.SoufianeDev.TaskApp.domain.dto.CreateTaskRequestDto;
import com.SoufianeDev.TaskApp.domain.dto.TaskDto;
import com.SoufianeDev.TaskApp.domain.dto.UpdateTaskRequestDto;
import com.SoufianeDev.TaskApp.domain.entity.Task;
import com.SoufianeDev.TaskApp.mapper.TaskMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper ;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(
            @Valid @RequestBody CreateTaskRequestDto createTaskRequestDto
    ) {
        CreateTaskRequest createTaskRequest = taskMapper.fromDto(createTaskRequestDto);
        Task task = taskService.createTask(createTaskRequest);
        TaskDto createdTaskDto = taskMapper.toDto(task);
        return new ResponseEntity<>(createdTaskDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> listTasks() {
        List<Task> tasks = taskService.listTasks();
        List<TaskDto> taskDtos = tasks.stream().map(taskMapper::toDto).toList();
        return ResponseEntity.ok(taskDtos);
    }
    @PutMapping(path = "/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable UUID taskId,
                                              @Valid @RequestBody UpdateTaskRequestDto updateTaskRequestDto)
    {
        UpdateTaskRequest updateTaskRequest= taskMapper.fromDto(updateTaskRequestDto);
        Task task = taskService.updateTask(taskId, updateTaskRequest);
        TaskDto taskDto = taskMapper.toDto(task);
        return ResponseEntity.ok(taskDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
