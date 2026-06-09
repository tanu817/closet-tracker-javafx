package com.mycompany.irr00_group_project.service;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.irr00_group_project.database.ClothingRepository;
import com.mycompany.irr00_group_project.database.TagRepository;
import com.mycompany.irr00_group_project.model.ClothingItem;
import com.mycompany.irr00_group_project.model.Tag;

public class SearchService {

    private final ClothingRepository clothingRepository;
    private final TagRepository tagRepository;

    public SearchService() {
        clothingRepository = new ClothingRepository();
        tagRepository = new TagRepository();
    }

    public List<String> getAllTagNames() {
        List<Tag> tags = tagRepository.getAllTags();
        List<String> names = new ArrayList<>();

        for (Tag t : tags) {
            names.add(t.getName());
        }

        return names;
    }

    public List<ClothingItem> searchByTagIds(List<Integer> selectedTagIds) {

        List<ClothingItem> results = new ArrayList<>();
        List<ClothingItem> allItems = clothingRepository.getAllItems();

        for (ClothingItem item : allItems) {

            List<Integer> itemTagIds =
                    tagRepository.getTagIdsOfItem(item.getId());

            boolean matches = true;

            for (Integer selectedId : selectedTagIds) {
                if (!itemTagIds.contains(selectedId)) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                results.add(item);
            }
        }

        return results;
    }
}