package com.gmail.aazavoykin.db.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "story_tag",
        joinColumns = @JoinColumn(name = "story_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "story_id")
    private List<Comment> comments = new ArrayList<>();
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime created;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updated;
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean rough;
}
