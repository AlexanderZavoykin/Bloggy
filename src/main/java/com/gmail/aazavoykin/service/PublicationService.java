package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.model.Publication;
import com.gmail.aazavoykin.repository.PublicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class PublicationService {

    @Autowired
    private final PublicationRepository publicationRepository;

    public List<Publication> getAll() {
        return (List<Publication>) publicationRepository.findAll();
    }

    public List<Publication> getAllByUserId(Long userId) {
        return publicationRepository.getAllByUser(userId);
    }

    public Optional<Publication> getById(Long id) {
        return publicationRepository.findById(id);
    }

    public Publication save(Publication publication) {
        return publicationRepository.save(publication);
    }

    public void delete(Long id) {
        publicationRepository.deleteById(id);
    }

}
