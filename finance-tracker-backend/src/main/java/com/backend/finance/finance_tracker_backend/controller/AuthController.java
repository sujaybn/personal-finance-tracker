package com.backend.finance.finance_tracker_backend.controller;

import com.backend.finance.finance_tracker_backend.dto.AuthRequest;
import com.backend.finance.finance_tracker_backend.dto.AuthResponse;
import com.backend.finance.finance_tracker_backend.dto.RegisterRequest;
import com.backend.finance.finance_tracker_backend.dto.RegisterResponse;
import com.backend.finance.finance_tracker_backend.model.User;
import com.backend.finance.finance_tracker_backend.repository.UserRepository;
import com.backend.finance.finance_tracker_backend.service.JwtService;
import com.backend.finance.finance_tracker_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.http.MediaType; 

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserService userService, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    
    @PostMapping("/register")
public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterRequest request) {
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)  // 🔥 Ensure JSON response
                .body(Map.of("message", "Email already exists"));
    }

    User user = new User(request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()));
    userRepository.save(user);

    String token = jwtService.generateToken(userService.loadUserByUsername(user.getEmail()));

    // ✅ **Ensure JSON response**
    Map<String, String> response = Map.of(
            "message", "User registered successfully",
            "token", token
    );

    return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)  // 🔥 **Force JSON response**
            .body(response);
}
    
    
    
    

    // ✅ LOGIN USER
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest authRequest) {
        try {
            // 🔥 FIX: Use authRequest.getEmail() instead of user.getEmail()
            String token = jwtService.generateToken(userService.loadUserByUsername(authRequest.getEmail()));
            return ResponseEntity.ok(new AuthResponse("Login successful", token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials", null));
        }
    }
}
