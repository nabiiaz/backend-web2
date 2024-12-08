package ufpr.web.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ufpr.web.entities.EquipmentCategory;

public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long>{
    List<EquipmentCategory> findByActive(Boolean active);
    Optional<EquipmentCategory> findByName(String name);
}
