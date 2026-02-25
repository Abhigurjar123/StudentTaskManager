package org.example.studenttaskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.studenttaskmanager.dto.TaskRequestDto;
import org.example.studenttaskmanager.dto.TaskResponseDto;
import org.example.studenttaskmanager.entity.Task;
import org.example.studenttaskmanager.entity.User;
import org.example.studenttaskmanager.exception.ResourceNotFoundException;
import org.example.studenttaskmanager.repository.TaskRepository;
import org.example.studenttaskmanager.repository.UserRepository;
import org.example.studenttaskmanager.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // ================= CREATE =================

    @Transactional
    @Override
    public TaskResponseDto createTask(TaskRequestDto dto) {

        User user = getCurrentUser();

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setDueDate(dto.getDueDate());
        task.setUser(user);

        return mapToDto(taskRepository.save(task));
    }

    // ================= GET MY TASKS =================

    @Override
    public List<TaskResponseDto> getMyTasks() {

        User user = getCurrentUser();

        return taskRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // ================= GET BY ID =================

    @Override
    public TaskResponseDto getTaskById(Long taskId) {

        User user = getCurrentUser();

        Task task = taskRepository
                .findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));

        return mapToDto(task);
    }

    // ================= UPDATE =================

    @Transactional
    @Override
    public TaskResponseDto updateTask(Long taskId, TaskRequestDto dto) {

        User user = getCurrentUser();

        Task task = taskRepository
                .findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setDueDate(dto.getDueDate());

        return mapToDto(taskRepository.save(task));
    }

    // ================= DELETE =================

    @Transactional
    @Override
    public void deleteTask(Long taskId) {

        User user = getCurrentUser();

        Task task = taskRepository
                .findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));

        taskRepository.delete(task);
    }

    // ================= ADMIN =================

    @Override
    public List<TaskResponseDto> getAllTasks() {

        return taskRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // ================= HELPER =================

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    private TaskResponseDto mapToDto(Task task) {

        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .build();
    }
}