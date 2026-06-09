package com.mycompany.irr00_group_project.controller;

import java.util.List;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.service.SearchService;

public class SearchController {

    private final SearchService searchService;

    public SearchController() {
        this.searchService = new SearchService();
    }

    public List<ClothingItem> searchByTags(List<Integer> selectedTagIds) {
        return searchService.searchByTagIds(selectedTagIds);
    }

    public List<String> getAllTagNames() {
        return searchService.getAllTagNames();
    }
}