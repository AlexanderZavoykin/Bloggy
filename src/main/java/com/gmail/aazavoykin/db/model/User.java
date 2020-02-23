package com.gmail.aazavoykin.db.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "nickname_ui", columnNames = {"nickname"}),
                @UniqueConstraint(name = "username_ui", columnNames = {"username"}),
                @UniqueConstraint(name = "email_ui", columnNames = {"email"}),
        })
@SequenceGenerator(name = "user_seq", initialValue = 1000000, allocationSize = 1)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @Column(name = "user_id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Story> stories;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String username;

    @Column(columnDefinition = "VARCHAR(60)", nullable = false)
    private String password;

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String nickname;

    @Column(columnDefinition = "VARCHAR(120)", nullable = false)
    private String email;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()", nullable = false)
    private LocalDateTime registered;

    @Column(columnDefinition = "VARCHAR(255)")
    private String info;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean enabled;

}
