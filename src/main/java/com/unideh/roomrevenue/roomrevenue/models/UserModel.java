package com.unideh.roomrevenue.roomrevenue.models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "lastname", nullable = false, length = 100)
    private String lastname;

    @Column(name = "username", nullable = false, length = 100)
    private String user;


    @Column(name = "password", nullable = false, length = 250)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 250)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;
}
