package org.example.studenttaskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.studenttaskmanager.dto.TaskRequestDto;
import org.example.studenttaskmanager.dto.TaskResponseDto;
import org.example.studenttaskmanager.entity.Task;
import org.example.studenttaskmanager.entity.User;
import org.example.studenttaskmanager.repository.TaskRepository;
import org.example.studenttaskmanager.repository.UserRepository;
import org.example.studenttaskmanager.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    

    @Transactional
    @Override
    public TaskResponseDto createTask(Long userId, TaskRequestDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + userId)
                );

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setDueDate(dto.getDueDate());
        task.setUser(user);

        Task savedTask = taskRepository.save(task);

        return mapToDto(savedTask);
    }

    


    @Override
    public List<TaskResponseDto> getAllTasksByUser(Long userId) {

        return taskRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    


    @Override
    public TaskResponseDto getTaskById(Long taskId, Long userId) {

        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found")
                );

        return mapToDto(task);
    }

    

    @Transactional
    @Override
    public TaskResponseDto updateTask(Long taskId, Long userId, TaskRequestDto dto) {

        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found")
                );

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setDueDate(dto.getDueDate());

        // save() optional (dirty checking), but explicit clearer
        Task updatedTask = taskRepository.save(task);

        return mapToDto(updatedTask);
    }

    

    @Transactional
    @Override
    public void deleteTaskById(Long taskId, Long userId) {

        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found")
                );

        taskRepository.delete(task);
    }

    // ---------------- MAPPER ----------------

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
