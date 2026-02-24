package org.example.studenttaskmanager.service;

import org.example.studenttaskmanager.dto.AuthRequestDto;
import org.example.studenttaskmanager.dto.AuthResponseDto;
import org.example.studenttaskmanager.dto.UserRequestDto;
import org.example.studenttaskmanager.dto.UserResponseDto;
import org.example.studenttaskmanager.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserService {
    UserResponseDto getById(long id);
    List<UserResponseDto> getAllUsers();
    void deleteById(long id);
    UserResponseDto updateUser( long id,UserRequestDto requestDto);
    UserResponseDto register(UserRequestDto userRequestDto);
    AuthResponseDto login(AuthRequestDto authRequestDto);
}
