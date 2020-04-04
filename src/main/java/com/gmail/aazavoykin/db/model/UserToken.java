package com.gmail.aazavoykin.db.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "user_token", uniqueConstraints = {@UniqueConstraint(name = "user_id_ui", columnNames = {"user_id"})})
@SequenceGenerator(name = "user_token_seq", initialValue = 1000000, allocationSize = 1)
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_token_seq")
    @Column(name = "user_token_id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_token_user_fk"))
    private User user;
    @Column(columnDefinition = "VARCHAR(36)", nullable = false)
    private String token;
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;
}
