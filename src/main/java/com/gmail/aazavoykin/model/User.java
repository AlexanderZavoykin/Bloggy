package com.gmail.aazavoykin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@SequenceGenerator(name = "seq", initialValue = 1000000, allocationSize = 1)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "user_id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Story> stories;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Comment> comments;

    @NotBlank(message = "Username cannot be blank")
    @Pattern(message = "Bad formed username",
            regexp = "^[a-zA-Z0-9]{8,20}$")
    @Size(min = 8, max = 20, message = "Username should contain from 8 up to 20 symbols which can be only letters or numbers")
    @Column(columnDefinition = "VARCHAR(20)", nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @NotBlank(message = "Password cannot be blank")
    @Pattern(message = "Bad formed password. Password should contain at least one capital letter, " +
            "at least one number and at least one special symbol of these: '@', '#', '$', '%', '^', '&', '+', '='.",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$)$")
    @Size(min = 8, max = 20, message = "Password should contain from 8 up to 20 symbols")
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @NotBlank(message = "Nickname cannot be blank")
    @Pattern(message = "Bad formed nickname",
            regexp = "^[a-zA-Z0-9_-]{8,20}$")
    @Size(min = 8, max = 20, message = "Nickname should contain from 8 up to 20 symbols (letters, numbers, '_', '-'")
    @Column(columnDefinition = "VARCHAR(20)", nullable = false, unique = true)
    private String nickname;

    @Email(message = "Email address has invalid format",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @Size(max = 120, message = "Password should contain up to 80 symbols")
    @Column(columnDefinition = "VARCHAR(120)", nullable = false, unique = true)
    private String email;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()", nullable = false)
    private LocalDateTime registered;

    @Length(max = 255, message = "User info should contain up to 255 symbols")
    @Column(columnDefinition = "VARCHAR(255)")
    private String info;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean enabled;

}
