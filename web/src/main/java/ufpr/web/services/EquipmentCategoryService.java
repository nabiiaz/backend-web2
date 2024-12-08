package ufpr.web.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import ufpr.web.entities.EquipmentCategory;
import ufpr.web.repositories.EquipmentCategoryRepository;
import ufpr.web.types.dtos.EquipmentCategoryDTO;

@Service
public class EquipmentCategoryService {

    @Autowired
    private EquipmentCategoryRepository equipmentCategoryRepository;

    @Transactional
    public EquipmentCategoryDTO createCategory(EquipmentCategoryDTO categoryDTO) {
        EquipmentCategory category = new EquipmentCategory();
        category.setName(categoryDTO.getName());
        category.setActive(true);

        EquipmentCategory saved = equipmentCategoryRepository.save(category);
        return new EquipmentCategoryDTO(saved.getId(), saved.getName());
    }

    @Transactional
    public void updateCategory(Long id, EquipmentCategoryDTO categoryDTO) {
        EquipmentCategory category = equipmentCategoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        category.setName(categoryDTO.getName());

        equipmentCategoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        EquipmentCategory category = equipmentCategoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        category.setActive(false);
        equipmentCategoryRepository.save(category);
    }

    public EquipmentCategoryDTO getCategory(Long id) {
        EquipmentCategory category = equipmentCategoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return new EquipmentCategoryDTO(category.getId(), category.getName());
    }


    public EquipmentCategory getCategoryEntity(Long id) {
        EquipmentCategory category = equipmentCategoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return category;
    }

    public List<EquipmentCategoryDTO> getCategories() {
        List<EquipmentCategory> categories = equipmentCategoryRepository.findAll();
        List<EquipmentCategoryDTO> categoryDTOs = new ArrayList<>();

        for (EquipmentCategory e : categories) {
            categoryDTOs.add(new EquipmentCategoryDTO(e.getId(), e.getName()));
        }

        return categoryDTOs;
    }

    public List<EquipmentCategoryDTO> getActiveCategories() {
        List<EquipmentCategory> categories = equipmentCategoryRepository.findByActive(true);
        List<EquipmentCategoryDTO> categoryDTOs = new ArrayList<>();

        for (EquipmentCategory e : categories) {
            categoryDTOs.add(new EquipmentCategoryDTO(e.getId(), e.getName()));
        }

        return categoryDTOs;
    }

    public EquipmentCategory findByName(String name) {
        return equipmentCategoryRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

}
