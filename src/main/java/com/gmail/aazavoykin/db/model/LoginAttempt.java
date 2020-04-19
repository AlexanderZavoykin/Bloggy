package com.gmail.aazavoykin.db.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "login_attempt")
@SequenceGenerator(name = "login_attempt_seq", initialValue = 1000000, allocationSize = 1)
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "login_attempt_seq")
    @Column(name = "login_attempt_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "login_attempt_user_fk"))
    private User user;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime attempted;
    @Column(nullable = false)
    private boolean success;

    @Override
    public String toString() {
        return "LoginAttempt{" +
            "id=" + id +
            ", user.id=" + user.getId() +
            ", attempted=" + attempted +
            ", success=" + success +
            '}';
    }
}
