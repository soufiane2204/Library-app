package com.soufiane.Library.service;

import com.soufiane.Library.DTO.AuthRequest;
import com.soufiane.Library.DTO.AuthResponse;
import com.soufiane.Library.model.Role;
import com.soufiane.Library.model.User;
import com.soufiane.Library.repository.UserRepo;
import com.soufiane.Library.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthService authService;

    private User existingUser;
    private AuthRequest request;

    @BeforeEach
    void setUp() {
        existingUser =new User();
        existingUser.setEmail("soufiane@gmail.com");
        existingUser.setPassword("hashedPassword123");
        existingUser.setRole(Role.ROLE_USER);

        request = new AuthRequest();
        request.setEmail("soufiane@gmail.com");
        request.setPassword("123456");
    }

    //-----register() tests-------------
    @Test
    void register_ShouldCreateUser_WhenEmailNotToken(){

        when(userRepo.findByEmail("soufiane@gmail.com")).thenReturn(Optional.empty());

        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword123"); // return a fake hashed version — we don't need real bcrypt here

        when(userRepo.save(any(User.class))).thenReturn(existingUser);

        when(jwtUtil.generateToken(anyString(),anyString())).thenReturn("fakeToken123");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("fakeToken123",response.getToken());
        assertEquals("ROLE_USER",response.getRole());

        verify(userRepo,times(1)).save(any(User.class));
        verify(passwordEncoder,times(1)).encode("123456");

    }
    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {

        when(userRepo.findByEmail("soufiane@gmail.com"));
        assertThrows(RuntimeException.class,
                () -> authService.register(request));

        verify(userRepo,never()).save(any(User.class));

        verify(passwordEncoder, never()).encode(anyString());
    }


    //----- login() tests -------
    @Test
    void  login_ShouldReturnToken_WhenCredentialsAreCorrect() {
        when(userRepo.findByEmail("soufiane@gmail.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("123456", "hashedPassword123")).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("fakeToken123");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("fakeToken123", response.getToken());
        assertEquals("ROLE_USER", response.getRole());

        verify(jwtUtil, times(1)).generateToken(anyString(), anyString());

    }
    @Test
    void login_ShouldThrowException_WhenPasswordIsWrong() {

        when(userRepo.findByEmail("soufiane@gmail.com")).thenReturn(Optional.of(existingUser));

        when(passwordEncoder.matches("123456", "hashedPassword123")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            authService.login(request);
        });

        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }
}












