package org.example.userauthenticationservice_dec2025.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequest {
    private String email;
    private String password;
}
