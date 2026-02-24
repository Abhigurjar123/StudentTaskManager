package org.example.studenttaskmanager.dto;

import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.studenttaskmanager.entity.enums.Role;

@Getter
@Setter
@Builder
public class AuthResponseDto {

        private String token;
        private String email;
        private String role;

    }

