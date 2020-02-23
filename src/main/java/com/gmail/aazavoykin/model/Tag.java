package com.gmail.aazavoykin.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @ManyToMany(mappedBy = "tags")
    private List<Story> stories = new ArrayList<>();

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()", nullable = false)
    private LocalDateTime created;

    @NotBlank(message = "Tag name can not be blank")
    @Pattern(message = "Tag name can contain only lowercase letters",
            regexp = "^[a-z]$")
    @Column(nullable = false)
    @Length(min = 6, max = 30, message = "Tag name should have 6 .. 30 characters")
    private String name;

    public Tag(String name) {
       this.name = name;
    }

}
