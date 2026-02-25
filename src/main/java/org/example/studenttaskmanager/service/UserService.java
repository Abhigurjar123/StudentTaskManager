package org.example.studenttaskmanager.service;

import org.example.studenttaskmanager.dto.AuthRequestDto;
import org.example.studenttaskmanager.dto.AuthResponseDto;
import org.example.studenttaskmanager.dto.UserRequestDto;
import org.example.studenttaskmanager.dto.UserResponseDto;
import org.example.studenttaskmanager.entity.enums.Role;

import java.util.List;

public interface UserService {

    // ================= AUTH =================
    UserResponseDto register(UserRequestDto userRequestDto);
    AuthResponseDto login(AuthRequestDto authRequestDto);

    // ================= USER =================
    UserResponseDto getCurrentUserProfile();
    UserResponseDto updateCurrentUser(UserRequestDto requestDto);

    // ================= ADMIN =================
    UserResponseDto getById(Long id);
    List<UserResponseDto> getAllUsers();
    void deleteById(Long id);
    UserResponseDto updateUserRole(Long id, Role role);
}