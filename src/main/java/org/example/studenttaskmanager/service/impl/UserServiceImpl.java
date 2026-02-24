package org.example.studenttaskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.studenttaskmanager.dto.AuthRequestDto;
import org.example.studenttaskmanager.dto.AuthResponseDto;
import org.example.studenttaskmanager.dto.UserRequestDto;
import org.example.studenttaskmanager.dto.UserResponseDto;
import org.example.studenttaskmanager.entity.User;
import org.example.studenttaskmanager.entity.enums.Role;
import org.example.studenttaskmanager.repository.UserRepository;
import org.example.studenttaskmanager.security.CustomUserDetailsService;
import org.example.studenttaskmanager.security.JwtService;
import org.example.studenttaskmanager.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @Override
    public UserResponseDto register(UserRequestDto userRequestDto) {
        String email = userRequestDto.getEmail().toLowerCase();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User Already Registered!");
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
    public UserResponseDto getById(long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No user Found with " + id));
        return mapToDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
          List<User> users= userRepository.findAll();
          return  users.stream()
                  .map(this::mapToDto)
                  .toList();
    }

    @Override
    public void deleteById(long id) {
        User user= userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User Not Found with id:"+id));
        userRepository.deleteById(id);

    }

    @Override
    public UserResponseDto updateUser(long id, UserRequestDto requestDto) {
        User user=userRepository.findById(id)
                               .orElseThrow(()->new RuntimeException("User not found with id:"+id));
        String email= requestDto.getEmail().toLowerCase();
        if (!user.getEmail().equals(email) && userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        user.setName(requestDto.getName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        User updatedUser= userRepository.save(user);
        return  mapToDto(updatedUser);

    }


    @Override
    public AuthResponseDto login(AuthRequestDto authRequestDto) {

        // 1️⃣ Authenticate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.getEmail(),
                        authRequestDto.getPassword()
                )
        );

        // 2️⃣ Fetch user from DB
        User user = userRepository.findByEmail(authRequestDto.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // 3️⃣ Load UserDetails (for token generation)
        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(user.getEmail());

        // 4️⃣ Generate JWT
        String token = jwtService.generateToken(userDetails);

        // 5️⃣ Return response
        return AuthResponseDto.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
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
