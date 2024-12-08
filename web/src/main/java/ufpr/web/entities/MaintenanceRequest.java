package ufpr.web.entities;


import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufpr.web.types.enums.RequestStatus;

@Entity
@Table(name = "request")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String equipmentDescription;

    @ManyToOne
    @JoinColumn(name= "equipment_category_id")
    private EquipmentCategory equipmentCategory;

    private String equipmentDefect;

    private Double quote;

    @Enumerated(EnumType.ORDINAL)
    private RequestStatus status;

    private LocalDateTime registryDate;

    @Column(nullable = true)
    private String maintenanceDescription;

    @Column(nullable = true)
    private String customerOrientation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Customer customer; 

    private Long employeeId;

    @OneToMany(mappedBy = "maintenanceRequest")
    private List<MaintenanceRequestHistory> history;
}
