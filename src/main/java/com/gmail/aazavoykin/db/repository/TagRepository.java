package com.gmail.aazavoykin.db.repository;

import com.gmail.aazavoykin.db.model.Tag;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    List<Tag> getAllByOrderByName();
    Tag getByNameIgnoreCase(String name);
    @Modifying
    @Query(value = "delete from tag t where t.tag_id not in (select st.tag_id from story_tag st)", nativeQuery = true)
    void removeOrphans();
}
