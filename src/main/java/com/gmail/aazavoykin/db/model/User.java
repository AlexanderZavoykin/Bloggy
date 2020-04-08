package com.gmail.aazavoykin.db.model;

import com.gmail.aazavoykin.db.model.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "users",
    uniqueConstraints = {@UniqueConstraint(name = "nickname_ui", columnNames = {"nickname"}),
        @UniqueConstraint(name = "email_ui", columnNames = {"email"})})
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
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), foreignKey = @ForeignKey(name = "role_user_fk"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String email;
    @Column(columnDefinition = "VARCHAR(60)", nullable = false)
    private String password;
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String nickname;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime registered;
    @Column(columnDefinition = "VARCHAR(255)")
    private String info;
    @Column(columnDefinition = "BOOLEAN", nullable = false)
    private boolean enabled;
}
