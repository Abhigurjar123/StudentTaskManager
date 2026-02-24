package org.example.studenttaskmanager.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.studenttaskmanager.entity.enums.Priority;
import org.example.studenttaskmanager.entity.enums.Status;

import java.time.LocalDate;

@Getter
@Builder
public class TaskResponseDto {

    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private LocalDate dueDate;
}