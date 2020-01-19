package com.gmail.aazavoykin.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@SequenceGenerator(name = "seq", initialValue = 1000000, allocationSize = 1)
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "story_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "story_tag",
            joinColumns = @JoinColumn(name = "story_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "story_id")
    private List<Comment> comments;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()", nullable = false)
    private LocalDateTime created;

    @NotBlank(message = "Title can not be blank")
    @Column(nullable = false)
    @Length(min = 6, max = 255, message = "Title should contain from 6 up to 255 symbols")
    private String title;

    @NotBlank(message = "Body can not be blank")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

}
