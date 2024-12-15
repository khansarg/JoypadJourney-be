package com.example.joypadjourney.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", uniqueConstraints=@UniqueConstraint(columnNames = "username"))
public class User{
    
    @NotNull
    @Id
    private String username;
    @NotNull
    private String password;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;
}
