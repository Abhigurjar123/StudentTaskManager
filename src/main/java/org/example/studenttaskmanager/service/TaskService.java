package org.example.studenttaskmanager.service;

import org.example.studenttaskmanager.dto.TaskRequestDto;
import org.example.studenttaskmanager.dto.TaskResponseDto;


import java.util.List;


public interface TaskService {

    TaskResponseDto createTask(Long userId, TaskRequestDto requestDto);

    TaskResponseDto updateTask(Long taskId, Long userId, TaskRequestDto requestDto);

    List<TaskResponseDto> getAllTasksByUser(Long userId);

    TaskResponseDto getTaskById(Long taskId, Long userId);

    void deleteTaskById(Long taskId, Long userId);
}
