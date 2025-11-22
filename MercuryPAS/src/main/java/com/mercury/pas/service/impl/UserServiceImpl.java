package com.mercury.pas.service.impl;

import com.mercury.pas.model.dto.UpdateProfileRequest;
import com.mercury.pas.model.dto.UserResponse;
import com.mercury.pas.model.entity.User;
import com.mercury.pas.repository.UserRepository;
import com.mercury.pas.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.map(user, UserResponse.class);
    }

    @Override
    public UserResponse updateProfile(UpdateProfileRequest request) {
    	
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setDob(request.getDob());
        user.setLicenseNumber(request.getLicenseNumber());
        userRepository.save(user);
        return mapper.map(user, UserResponse.class);
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(u -> mapper.map(u, UserResponse.class))
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}

