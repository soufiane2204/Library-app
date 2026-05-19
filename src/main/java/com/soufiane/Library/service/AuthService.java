package com.soufiane.Library.service;

import com.soufiane.Library.DTO.AuthRequest;
import com.soufiane.Library.DTO.AuthResponse;
import com.soufiane.Library.model.Role;
import com.soufiane.Library.model.User;
import com.soufiane.Library.repository.UserRepo;
import com.soufiane.Library.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired private UserRepo userRepo;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;


    public AuthResponse register(AuthRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepo.save(user);
        String token = jwtUtil.generateToken(user.getEmail() , user.getPassword());
        return new AuthResponse(token, user.getRole().name());
    }



    public AuthResponse login(AuthRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("invalid email or password");
        }
        String token = jwtUtil.generateToken(user.getEmail() , user.getRole().name());
        return new AuthResponse(token, user.getRole().name());
    }



}
