package ufpr.web.controllers;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ufpr.web.entities.Customer;
import ufpr.web.entities.Employee;
import ufpr.web.entities.MaintenanceRequest;
import ufpr.web.entities.MaintenanceRequestHistory;
import ufpr.web.services.CustomerService;
import ufpr.web.services.EmployeeService;
import ufpr.web.services.EquipmentCategoryService;
import ufpr.web.services.MaintenanceHistoryService;
import ufpr.web.services.MaintenanceRequestService;
import ufpr.web.types.dtos.EquipmentCategoryDTO;
import ufpr.web.types.dtos.MaintenanceHistoryDTO;
import ufpr.web.types.dtos.MaintenanceRequestDTO;
import ufpr.web.types.dtos.OpenRequestDTO;
import ufpr.web.types.dtos.QuoteRequestDTO;
import ufpr.web.types.dtos.RequestWithHistoryAndCategoryDTO;
import ufpr.web.types.enums.RequestStatus;

@RestController
public class RequestController {

    @Autowired
    private MaintenanceRequestService maintenanceRequestService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MaintenanceHistoryService maintenanceHistoryService;

    @Autowired
    private EquipmentCategoryService equipmentCategoryService;
    
    @GetMapping("/requests")
    public List<MaintenanceRequestDTO> getRequestsForClient(@RequestParam Long userId) {
        List<MaintenanceRequest> requests = maintenanceRequestService.findRequestsByUserId(userId);
        
        return requests.stream()
        .map(request -> MaintenanceRequestDTO.builder()
                .id(request.getId())
                .registryDate(request.getRegistryDate())
                .equipmentDescription(request.getEquipmentDescription())
                .status(request.getStatus())
                .quote(request.getQuote())
                .equipmentCategory(request.getEquipmentCategory().getId())
                .equipmentDefect(request.getEquipmentDefect())
                .customerId(userId)
                .build()
        )
        .collect(Collectors.toList());

    }

    @PostMapping("/requests")
    public MaintenanceRequestDTO createMaintenanceRequest(@RequestBody MaintenanceRequestDTO requestDTO) {

        MaintenanceRequest request = new MaintenanceRequest();
        request.setEquipmentDescription(requestDTO.getEquipmentDescription());
        request.setEquipmentCategory(equipmentCategoryService.getCategoryEntity(requestDTO.getEquipmentCategory()));
        request.setEquipmentDefect(requestDTO.getEquipmentDefect());
        request.setRegistryDate(LocalDateTime.now());
        request.setStatus(RequestStatus.ABERTA);
        
        Customer customer = customerService.customer(requestDTO.getCustomerId());
        request.setCustomer(customer);

        MaintenanceRequest savedRequest = maintenanceRequestService.save(request);

        maintenanceHistoryService.save(MaintenanceRequestHistory.builder()
            .maintenanceRequest(savedRequest)
            .status(RequestStatus.ABERTA)
            .action("Created")
            .actionDate(new Date(System.currentTimeMillis()))
            .build());

        return MaintenanceRequestDTO.builder()
            .id(savedRequest.getId())
            .equipmentDescription(savedRequest.getEquipmentDescription())
            .equipmentCategory(request.getEquipmentCategory().getId())
            .equipmentDefect(savedRequest.getEquipmentDefect())
            .quote(savedRequest.getQuote())
            .status(savedRequest.getStatus())
            .registryDate(savedRequest.getRegistryDate())
            .customerId(savedRequest.getCustomer().getId())
            .build();
    }

    @PutMapping("/requests/{requestId}/approve")
    public ResponseEntity<String> approveService(@PathVariable Long requestId) {
        MaintenanceRequest request = maintenanceRequestService.findById(requestId);
        request.setStatus(RequestStatus.APROVADA);

        maintenanceRequestService.save(request);

        maintenanceHistoryService.save(MaintenanceRequestHistory.builder()
            .maintenanceRequest(request)
            .status(RequestStatus.APROVADA)
            .action("Aprovada")
            .actionDate(new Date(System.currentTimeMillis()))
            .build());

        return ResponseEntity.ok().body("Serviço aprovado no valor de: " + request.getQuote());
    }

    @PutMapping("/requests/{requestId}/reject")
    public ResponseEntity<String> rejectService(@PathVariable Long requestId) {
        MaintenanceRequest request = maintenanceRequestService.findById(requestId);
        request.setStatus(RequestStatus.REJEITADA);

        maintenanceRequestService.save(request);

        maintenanceHistoryService.save(MaintenanceRequestHistory.builder()
            .maintenanceRequest(request)
            .status(RequestStatus.REJEITADA)
            .action("Rejeitada")
            .actionDate(new Date(System.currentTimeMillis()))
            .build());

            return ResponseEntity.ok().body("Serviço rejeitado");
    }

    @PutMapping("/requests/{requestId}/redeem")
    public ResponseEntity<String> redeemService(@PathVariable Long requestId) {
        MaintenanceRequest request = maintenanceRequestService.findById(requestId);
        request.setStatus(RequestStatus.APROVADA);

        maintenanceRequestService.save(request);

        maintenanceHistoryService.save(MaintenanceRequestHistory.builder()
            .maintenanceRequest(request)
            .status(RequestStatus.APROVADA)
            .action("Resgatada")
            .actionDate(new Date(System.currentTimeMillis()))
            .build());

            return ResponseEntity.ok().body("Serviço resgatado");
    }

    @PutMapping("/requests/{requestId}/pay")
    public ResponseEntity<String> payService(@PathVariable Long requestId) {
        MaintenanceRequest request = maintenanceRequestService.findById(requestId);
        request.setStatus(RequestStatus.PAGA);

        maintenanceRequestService.save(request);

        maintenanceHistoryService.save(MaintenanceRequestHistory.builder()
            .maintenanceRequest(request)
            .status(RequestStatus.APROVADA)
            .action("Pago")
            .actionDate(new Date(System.currentTimeMillis()))
            .build());

            return ResponseEntity.ok().body("Serviço pago");
    }

    @GetMapping("/requests/{requestId}")
    public RequestWithHistoryAndCategoryDTO getRequestById(@PathVariable Long requestId) {
        
        MaintenanceRequest request = maintenanceRequestService.findById(requestId);
        
        List<MaintenanceRequestHistory> history = maintenanceHistoryService.findByRequestId(requestId);
        
        MaintenanceRequestDTO maintenanceRequestDTO = MaintenanceRequestDTO.builder()
            .id(request.getId())
            .equipmentDescription(request.getEquipmentDescription())
            .equipmentCategory(request.getEquipmentCategory().getId())
            .equipmentDefect(request.getEquipmentDefect())
            .quote(request.getQuote())
            .status(request.getStatus())
            .registryDate(request.getRegistryDate())
            .customerId(request.getCustomer().getId())
            .build();

            List<MaintenanceHistoryDTO> historyDTOs = history.stream()
                .map(h -> MaintenanceHistoryDTO.builder()
                    .id(h.getId())
                    .requestId(h.getMaintenanceRequest().getId())
                    .status(h.getStatus())
                    .action(h.getAction())
                    .employeeId(h.getEmployee() != null ? h.getEmployee().getId() : -1)
                    .actionDate(h.getActionDate())
                    .build())
                .collect(Collectors.toList());

            EquipmentCategoryDTO categoryDTO = EquipmentCategoryDTO.builder()
                .id(request.getEquipmentCategory().getId())
                .name(request.getEquipmentCategory().getName())
                .build();

        return RequestWithHistoryAndCategoryDTO.builder()
            .maintenanceRequest(maintenanceRequestDTO)
            .history(historyDTOs)
            .equipmentCategory(categoryDTO)
            .build();
    }

    @GetMapping("/requests/open")
    public List<OpenRequestDTO> getOpenRequestsForEmployee() {
        List<MaintenanceRequest> openRequests = maintenanceRequestService.findRequestsByStatus(RequestStatus.ABERTA);

        return openRequests.stream()
            .map(request -> {
                Customer customer = customerService.customer(request.getCustomer().getId());

                return OpenRequestDTO.builder()
                    .id(request.getId())
                    .registryDate(request.getRegistryDate())
                    .customerName(customer.getName())
                    .equipmentDescription(request.getEquipmentDescription())
                    .build();
            })
            .collect(Collectors.toList());
    }

    @PutMapping("/requests/{requestId}/quote")
    public ResponseEntity<String> createQuote(@PathVariable Long requestId, @RequestBody QuoteRequestDTO quoteDTO) {
        
        MaintenanceRequest request = maintenanceRequestService.findById(requestId);
        Employee employee = employeeService.employee(quoteDTO.getEmployeeId());

        if (request.getStatus() == RequestStatus.ORÇADA) {
            return ResponseEntity.badRequest().body("Serviço já foi orçado");
        }

        request.setQuote(quoteDTO.getQuoteAmount());
        request.setStatus(RequestStatus.ORÇADA);
        MaintenanceRequest updatedRequest = maintenanceRequestService.save(request);

        maintenanceHistoryService.save(MaintenanceRequestHistory.builder()
            .maintenanceRequest(updatedRequest)
            .status(RequestStatus.ORÇADA)
            .action("Orçamento feito")
            .actionDate(new Date(System.currentTimeMillis()))
            .employee(employee)
            .build());

        return ResponseEntity.ok().body("Orçamento feito no valor de: " + quoteDTO.getQuoteAmount());
    }

}
