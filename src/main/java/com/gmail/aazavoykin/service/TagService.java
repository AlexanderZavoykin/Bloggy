package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.db.repository.TagRepository;
import com.gmail.aazavoykin.rest.dto.TagDto;
import com.gmail.aazavoykin.rest.dto.mapper.TagMapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class TagService {

    private final TagMapper tagMapper;
    private final TagRepository tagRepository;

    public List<TagDto> getAll() {
        return tagMapper.tagsToTagDtos(tagRepository.getAllByOrderByName());
    }
}
