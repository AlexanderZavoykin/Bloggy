package com.gmail.aazavoykin.repository;

import com.gmail.aazavoykin.model.Publication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends CrudRepository<Publication, Long> {

    List<Publication> getAllByUser(Long userId);

}
