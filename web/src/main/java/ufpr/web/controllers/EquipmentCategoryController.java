package ufpr.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ufpr.web.services.EquipmentCategoryService;
import ufpr.web.types.dtos.EquipmentCategoryDTO;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/categories")
public class EquipmentCategoryController {

    @Autowired
    private EquipmentCategoryService equipmentCategoryService;

    @PostMapping()
    public EquipmentCategoryDTO createCategory(@RequestBody EquipmentCategoryDTO categoryDTO) {
        return equipmentCategoryService.createCategory(categoryDTO);
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long id, @RequestBody EquipmentCategoryDTO categoryDTO) {
        equipmentCategoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        equipmentCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentCategoryDTO> getCategory(@PathVariable Long id) {
        EquipmentCategoryDTO categoryDTO = equipmentCategoryService.getCategory(id);
        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping()
    public ResponseEntity<List<EquipmentCategoryDTO>> getCategories() {
        List<EquipmentCategoryDTO> categoryDTOs = equipmentCategoryService.getCategories();
        return ResponseEntity.ok(categoryDTOs);
    }

    @GetMapping("/active")
    public ResponseEntity<List<EquipmentCategoryDTO>> getActiveCategories() {
        List<EquipmentCategoryDTO> categoryDTOs = equipmentCategoryService.getActiveCategories();
        return ResponseEntity.ok(categoryDTOs);
    }
}