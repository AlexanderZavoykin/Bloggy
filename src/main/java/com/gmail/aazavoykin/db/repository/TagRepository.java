package com.gmail.aazavoykin.db.repository;

import com.gmail.aazavoykin.db.model.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    List<Tag> getAllByOrderByName();

    Tag getByName(String name);
}
