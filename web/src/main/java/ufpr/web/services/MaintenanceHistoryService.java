package ufpr.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufpr.web.entities.MaintenanceRequestHistory;
import ufpr.web.repositories.MaintenanceHistoryRepository;

@Service
public class MaintenanceHistoryService {

    @Autowired
    private MaintenanceHistoryRepository requestHistoryRepository;

    public void save(MaintenanceRequestHistory history) {
        requestHistoryRepository.save(history);
    }

    public List<MaintenanceRequestHistory> findByRequestId(Long requestId) {
        return requestHistoryRepository.findByMaintenanceRequestId(requestId);
    }
}