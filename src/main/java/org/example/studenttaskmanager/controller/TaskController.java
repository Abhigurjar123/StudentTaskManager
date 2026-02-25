package org.example.studenttaskmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.studenttaskmanager.dto.TaskRequestDto;
import org.example.studenttaskmanager.dto.TaskResponseDto;
import org.example.studenttaskmanager.service.TaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // ================= USER =================

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public TaskResponseDto createTask(@Valid @RequestBody TaskRequestDto dto) {
        return taskService.createTask(dto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public List<TaskResponseDto> getMyTasks() {
        return taskService.getMyTasks();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public TaskResponseDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public TaskResponseDto updateTask(
            @PathVariable Long id,@Valid
            @RequestBody TaskRequestDto dto) {
        return taskService.updateTask(id, dto);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "Task deleted successfully";
    }

    // ================= ADMIN =================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<TaskResponseDto> getAllTasks() {
        return taskService.getAllTasks();
    }
}