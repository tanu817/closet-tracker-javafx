package com.mycompany.irr00_group_project.service;

import com.mycompany.irr00_group_project.database.ClothingRepository;
import com.mycompany.irr00_group_project.model.ClothingItem;

import java.util.List;

public class SearchService {

    private final ClothingRepository clothingRepository;

    public SearchService() {
        this.clothingRepository = new ClothingRepository();
    }

    public List<ClothingItem> searchByTags(List<String> tags) {
        return clothingRepository.findByTags(tags);
    }
}