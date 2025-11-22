package com.mercury.pas.service;

import com.mercury.pas.model.dto.UpdateProfileRequest;
//import com.mercury.pas.model.dto.UserDtos;
import com.mercury.pas.model.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getCurrentUser();
    UserResponse updateProfile(UpdateProfileRequest request);
    List<UserResponse> getAll();
    void deleteById(Long id);
}

