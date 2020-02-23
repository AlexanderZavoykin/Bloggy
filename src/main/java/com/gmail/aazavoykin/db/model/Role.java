package com.gmail.aazavoykin.db.model;


import com.gmail.aazavoykin.db.model.enums.RoleName;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "role")
@SequenceGenerator(name = "role_seq", initialValue = 1000000, allocationSize = 1)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @Column(name = "role_id")
    private Long id;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @Enumerated
    private RoleName name;

}
