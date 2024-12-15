package com.example.joypadjourney.model.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @Size(max=50)
    private String username;

    @NotBlank
    @Size(max=25)
    private String password;
    
    @Size(max=255)
    private String email;


}
