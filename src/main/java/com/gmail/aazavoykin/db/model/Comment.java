package com.gmail.aazavoykin.db.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@SequenceGenerator(name = "comment_seq", initialValue = 1000000, allocationSize = 1)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "comment_seq")
    @Column(name = "comment_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()", nullable = false)
    private LocalDateTime created;
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String body;
}
