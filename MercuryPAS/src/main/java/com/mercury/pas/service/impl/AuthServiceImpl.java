package com.mercury.pas.service.impl;

import com.mercury.pas.model.dto.AuthResponse;
import com.mercury.pas.model.dto.LoginRequest;
import com.mercury.pas.model.dto.RegisterRequest;
import com.mercury.pas.model.dto.UserResponse;
import com.mercury.pas.model.entity.User;
import com.mercury.pas.repository.UserRepository;
import com.mercury.pas.security.CustomUserDetailsService;
import com.mercury.pas.security.JwtTokenProvider;
import com.mercury.pas.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final ModelMapper mapper;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.mapper = mapper;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .dob(request.getDob())
                .licenseNumber(request.getLicenseNumber())
                .build();

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = tokenProvider.generateToken(userDetails, user.getRole().name());
        UserResponse userResponse = mapper.map(user, UserResponse.class);

        return new AuthResponse(token, userResponse);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = tokenProvider.generateToken(userDetails, user.getRole().name());
        UserResponse userResponse = mapper.map(user, UserResponse.class);

        return new AuthResponse(token, userResponse);
    }
}

