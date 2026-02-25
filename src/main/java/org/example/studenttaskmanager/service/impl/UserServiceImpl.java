package org.example.studenttaskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.studenttaskmanager.dto.AuthRequestDto;
import org.example.studenttaskmanager.dto.AuthResponseDto;
import org.example.studenttaskmanager.dto.UserRequestDto;
import org.example.studenttaskmanager.dto.UserResponseDto;
import org.example.studenttaskmanager.entity.User;
import org.example.studenttaskmanager.entity.enums.Role;
import org.example.studenttaskmanager.exception.BadRequestException;
import org.example.studenttaskmanager.exception.ResourceNotFoundException;
import org.example.studenttaskmanager.repository.UserRepository;
import org.example.studenttaskmanager.security.CustomUserDetailsService;
import org.example.studenttaskmanager.security.JwtService;
import org.example.studenttaskmanager.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // ================= AUTH =================

    @Override
    public UserResponseDto register(UserRequestDto userRequestDto) {

        String email = userRequestDto.getEmail().toLowerCase();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("User already registered!");
        }

        User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return mapToDto(savedUser);
    }

    @Override
    public AuthResponseDto login(AuthRequestDto authRequestDto) {

        String email = authRequestDto.getEmail().toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        authRequestDto.getPassword()
                )
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtService.generateToken(userDetails);

        return AuthResponseDto.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    // ================= CURRENT USER =================

    @Override
    public UserResponseDto getCurrentUserProfile() {
        User user = getCurrentUser();
        return mapToDto(user);
    }

    @Override
    public UserResponseDto updateCurrentUser(UserRequestDto requestDto) {

        User user = getCurrentUser();

        String email = requestDto.getEmail().toLowerCase();

        if (!user.getEmail().equals(email) &&
                userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Email already in use");
        }

        user.setName(requestDto.getName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        User updatedUser = userRepository.save(user);

        return mapToDto(updatedUser);
    }

    // ================= ADMIN =================

    @Override
    public UserResponseDto getById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No user found with id: " + id));

        return mapToDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }

    @Override
    public UserResponseDto updateUserRole(Long id, Role role) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setRole(role);

        User updatedUser = userRepository.save(user);

        return mapToDto(updatedUser);
    }

    // ================= HELPER =================

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Logged-in user not found"));
    }

    private UserResponseDto mapToDto(User user) {

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}