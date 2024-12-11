package com.example.joypadjourney.model.Request;

import jakarta.validation.constraints.Email;
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
public class RegisterCustomerRequest {
    @NotBlank
    @Size(max=50)
    private String username;
    @NotBlank
    @Size(max=25)
    private String password;
    @NotBlank
    @Size(max=100)
    @Email
    private String email;
    @NotBlank
    @Size(max=15)
    private String phoneNum;
    @NotBlank
    @Size(max=50)
    private String firstName;
    @NotBlank
    @Size(max=50)
    private String lastName;
}
