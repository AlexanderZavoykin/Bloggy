package com.gmail.aazavoykin.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publication_id")
    private Long id;

   @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    private LocalDateTime created;

    @NotBlank
    private String title;

    @ManyToMany
    private List<Tag> tags;

    @NotBlank
    private String body;

    @OneToMany
    private List<Comment> comments;

}
