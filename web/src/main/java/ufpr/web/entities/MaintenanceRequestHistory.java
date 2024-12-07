package ufpr.web.entities;

import java.sql.Date;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufpr.web.types.enums.RequestStatus;

@Entity
@Table(name = "request_history")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceRequestHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "request_id")
    private MaintenanceRequest maintenanceRequest;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private String action;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = true)
    private Employee employee;

    private Date actionDate;
}