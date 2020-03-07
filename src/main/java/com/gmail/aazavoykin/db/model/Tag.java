package com.gmail.aazavoykin.db.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "name_ui", columnNames = {"name"}))
@SequenceGenerator(name = "tag_seq", initialValue = 1000000, allocationSize = 1)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tag_seq")
    @Column(name = "tag_id")
    private Long id;
    @ManyToMany(mappedBy = "tags")
    private List<Story> stories = new ArrayList<>();
    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()", nullable = false)
    private LocalDateTime created;
    @Column(nullable = false, columnDefinition = "VARCHAR(30)")
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}
