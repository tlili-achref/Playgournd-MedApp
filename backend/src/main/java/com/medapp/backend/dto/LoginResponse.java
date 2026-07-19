package com.medapp.backend.dto;

import com.medapp.backend.model.Role;

public record LoginResponse(String accessToken , Role role) {

 
    
}
