package com.gmail.aazavoykin.db.model;

import com.gmail.aazavoykin.db.model.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "nickname_ui", columnNames = {"nickname"}),
                @UniqueConstraint(name = "email_ui", columnNames = {"email"}),
        })
@SequenceGenerator(name = "user_seq", initialValue = 1000000, allocationSize = 1)
public class User implements UserDetails {

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

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Column(columnDefinition = "VARCHAR(120)", nullable = false)
    private String email;

    @Column(columnDefinition = "VARCHAR(60)", nullable = false)
    private String password;

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String nickname;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()", nullable = false)
    private LocalDateTime registered;

    @Column(columnDefinition = "VARCHAR(255)")
    private String info;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
}
