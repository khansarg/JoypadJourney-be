package com.example.joypadjourney.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(max=50)
    private String username;
    @NotNull
    @Size(max = 255)
    private String password;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    public String getPassword() {
        return password;
    }

    public String getUsername(){
        return username;
    }
   
    public Role getRole(){
        return role;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public void setUsername(String username){
        this.username=username;
    }
    public void setRole(Role role){
        this.role=role;
    }
}
