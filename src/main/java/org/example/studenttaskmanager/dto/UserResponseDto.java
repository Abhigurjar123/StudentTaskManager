package org.example.studenttaskmanager.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.studenttaskmanager.entity.enums.Role;

@Setter
@Getter
@Builder
public class UserResponseDto {
    private long id;
    private String name;
    private String email;
    private Role role;
}
