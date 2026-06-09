package com.mycompany.irr00_group_project.controller;

import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.service.SearchService;


import java.util.List;

public class SearchController {

    private final SearchService searchService;

    public SearchController() {
        this.searchService = new SearchService();
    }

    public List<ClothingItem> searchByTags(List<String> tags) {
        return searchService.searchByTags(tags);
    }

    public List<ClothingItem> searchByText(String query) {
        return searchService.searchByText(query);
    }
}