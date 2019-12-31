package com.gmail.aazavoykin.repository;

import com.gmail.aazavoykin.model.Publication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationRepository extends CrudRepository<Publication, Long> {
}
