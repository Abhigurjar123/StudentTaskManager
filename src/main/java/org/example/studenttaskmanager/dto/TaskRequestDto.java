package org.example.studenttaskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.studenttaskmanager.entity.enums.Priority;
import org.example.studenttaskmanager.entity.enums.Status;

import java.time.LocalDate;

@Getter
@Setter
public class TaskRequestDto {

    @NotBlank(message = "Title cannot be Blank")
    @Size(max = 100)
    private String title;

    @NotBlank(message = "Description cannot be Blank")
    @Size(max = 500)
    private String description;

    @NotNull(message = "Please Set Priority")
    private Priority priority;

    @NotNull(message = "Please Set Status")
    private Status status;

    @NotNull
    private LocalDate dueDate;
}
