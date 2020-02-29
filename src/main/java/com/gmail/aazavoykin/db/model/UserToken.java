package com.gmail.aazavoykin.db.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "user_token")
@SequenceGenerator(name = "user_token_seq", initialValue = 1000000, allocationSize = 1)
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @Column(name = "user_token_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "VARCHAR(16)", nullable = false)
    private String token;

    @Column(name = "expiry_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_DATE + INTERVAL '3 DAY'", nullable = false)
    private LocalDate expiryDate;

}
