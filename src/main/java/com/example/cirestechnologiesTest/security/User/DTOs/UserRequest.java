package com.example.cirestechnologiesTest.security.User.DTOs;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {
    private String firstName;

    private String lastName;

    private String birthDate;

    private String city;

    private String country;

    private String avatar;

    private String company;

    private String jobPosition;

    private String mobile;

    private String username;

    private String email;

    private String password;

    private String role;
}
