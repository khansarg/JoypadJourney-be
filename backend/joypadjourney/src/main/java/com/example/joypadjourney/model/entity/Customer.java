package com.example.joypadjourney.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "customer")
public class Customer {

    @Id
    private String username;

    @OneToOne
    @MapsId
    @JoinColumn(name = "username")
    private User user;

    @NotNull
    private String email;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
}
