package org.example.studenttaskmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.studenttaskmanager.dto.AuthRequestDto;
import org.example.studenttaskmanager.dto.AuthResponseDto;
import org.example.studenttaskmanager.dto.UserRequestDto;
import org.example.studenttaskmanager.dto.UserResponseDto;
import org.example.studenttaskmanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private  final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto requestDto){
        return new  ResponseEntity<>(userService.register(requestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto requestDto){
        return ResponseEntity.ok(userService.login(requestDto));
    }


}
