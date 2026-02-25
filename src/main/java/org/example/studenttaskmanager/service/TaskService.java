package org.example.studenttaskmanager.service;

import org.example.studenttaskmanager.dto.TaskRequestDto;
import org.example.studenttaskmanager.dto.TaskResponseDto;

import java.util.List;

public interface TaskService {

    // USER
    TaskResponseDto createTask(TaskRequestDto requestDto);

    TaskResponseDto updateTask(Long taskId, TaskRequestDto requestDto);

    List<TaskResponseDto> getMyTasks();

    TaskResponseDto getTaskById(Long taskId);

    void deleteTask(Long taskId);

    // ADMIN
    List<TaskResponseDto> getAllTasks();
}