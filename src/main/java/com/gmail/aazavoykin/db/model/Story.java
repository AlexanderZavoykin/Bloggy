package com.gmail.aazavoykin.db.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@SequenceGenerator(name = "story_seq", initialValue = 1000000, allocationSize = 1)
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "story_seq")
    @Column(name = "story_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "story_tag",
            joinColumns = @JoinColumn(name = "story_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "story_id")
    private List<Comment> comments = new ArrayList<>();

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()", nullable = false)
    private LocalDateTime created;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean rough;

}
