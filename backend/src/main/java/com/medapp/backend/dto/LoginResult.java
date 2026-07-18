package com.medapp.backend.dto;

import com.medapp.backend.model.Role;

public record LoginResult(String accessToken ,String refreshToken ,  Role role) {
    
}
