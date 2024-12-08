package ufpr.web.controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import ufpr.web.types.dtos.CustomerDTO;
import ufpr.web.types.dtos.EquipmentCategoryDTO;
import ufpr.web.types.dtos.FilteredRequestDTO;
import ufpr.web.types.dtos.MaintainDTO;
import ufpr.web.types.dtos.MaintenanceHistoryDTO;
import ufpr.web.types.dtos.MaintenanceRequestDTO;
import ufpr.web.types.dtos.OpenRequestDTO;
import ufpr.web.types.dtos.QuoteRequestDTO;
import ufpr.web.types.dtos.RedirectDTO;
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
            .action("Solicitação Aberta")
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

        if (request.getStatus() != RequestStatus.ORÇADA) {
            return ResponseEntity.badRequest().body("Serviço não foi orçado");
        }

        request.setStatus(RequestStatus.APROVADA);

        maintenanceRequestService.save(request);

        maintenanceHistoryService.save(MaintenanceRequestHistory.builder()
            .maintenanceRequest(request)
            .status(RequestStatus.APROVADA)
            .action("Orçamento Aprovado")
            .actionDate(new Date(System.currentTimeMillis()))
            .build());

        return ResponseEntity.ok().body("Orçamento Aprovado no valor de: " + request.getQuote());
    }

    @PutMapping("/requests/{requestId}/reject")
    public ResponseEntity<String> rejectService(@PathVariable Long requestId) {
        MaintenanceRequest request = maintenanceRequestService.findById(requestId);

        if (request.getStatus() != RequestStatus.ORÇADA) {
            return ResponseEntity.badRequest().body("Serviço não foi orçado");
        }

        request.setStatus(RequestStatus.REJEITADA);

        maintenanceRequestService.save(request);

        maintenanceHistoryService.save(MaintenanceRequestHistory.builder()
            .maintenanceRequest(request)
            .status(RequestStatus.REJEITADA)
            .action("Orçamento Rejeitado")
            .actionDate(new Date(System.currentTimeMillis()))
            .build());

            return ResponseEntity.ok().body("Orçamento rejeitado");
    }

    @PutMapping("/requests/{requestId}/redeem")
    public ResponseEntity<String> redeemService(@PathVariable Long requestId) {
        MaintenanceRequest request = maintenanceRequestService.findById(requestId);

        if (request.getStatus() != RequestStatus.REJEITADA) {
            return ResponseEntity.badRequest().body("Serviço não estava rejeitado");
        }

        request.setStatus(RequestStatus.APROVADA);

        maintenanceRequestService.save(request);

        maintenanceHistoryService.save(MaintenanceRequestHistory.builder()
            .maintenanceRequest(request)
            .status(RequestStatus.APROVADA)
            .action("Solicitação resgatada")
            .actionDate(new Date(System.currentTimeMillis()))
            .build());

            return ResponseEntity.ok().body("Solicitação resgatada");
    }

    @PutMapping("/requests/{requestId}/pay")
    public ResponseEntity<String> payService(@PathVariable Long requestId) {
        MaintenanceRequest request = maintenanceRequestService.findById(requestId);

        if (request.getStatus() != RequestStatus.ARRUMADA) {
            return ResponseEntity.badRequest().body("Serviço não foi arrumado");
        }

        request.setStatus(RequestStatus.PAGA);

        maintenanceRequestService.save(request);

        maintenanceHistoryService.save(MaintenanceRequestHistory.builder()
            .maintenanceRequest(request)
            .status(RequestStatus.PAGA)
            .action("Pago")
            .actionDate(new Date(System.currentTimeMillis()))
            .build());

            return ResponseEntity.ok().body("Solicitação paga");
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
            .maintenanceDescription(request.getMaintenanceDescription())
            .customerOrientation(request.getCustomerOrientation())
            .customerId(request.getCustomer().getId())
            .employeeId(request.getEmployeeId())
            .build();

            List<MaintenanceHistoryDTO> historyDTOs = history.stream()
                .map(h -> MaintenanceHistoryDTO.builder()
                    .id(h.getId())
                    .requestId(h.getMaintenanceRequest().getId())
                    .status(h.getStatus())
                    .action(h.getAction())
                    .employeeId(h.getEmployee() != null ? h.getEmployee().getId() : -1)
                    .actionDate(h.getActionDate())
                    .employeeDTO(h.getEmployee() != null ? employeeService.getEmployee(h.getEmployee().getId()) : null)
                    .build())
                .collect(Collectors.toList());

            EquipmentCategoryDTO categoryDTO = EquipmentCategoryDTO.builder()
                .id(request.getEquipmentCategory().getId())
                .name(request.getEquipmentCategory().getName())
                .build();

            CustomerDTO customerDTO = customerService.getCustomer(request.getCustomer() != null ? request.getCustomer().getId() : null);

        return RequestWithHistoryAndCategoryDTO.builder()
            .maintenanceRequest(maintenanceRequestDTO)
            .history(historyDTOs)
            .equipmentCategory(categoryDTO)
            .customer(customerDTO)
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
        request.setEmployeeId(employee.getId());
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

    @PutMapping("/requests/{requestId}/finish")
    public ResponseEntity<String> finishMaintenance(@PathVariable Long requestId, @RequestBody Long employeeId) {
        
        MaintenanceRequest request = maintenanceRequestService.findById(requestId);

        // if (request.getStatus() != RequestStatus.PAGA) {
        //     return ResponseEntity.badRequest().body("Serviço não foi pago");
        // }

        Employee employee = employeeService.employee(employeeId);

        request.setStatus(RequestStatus.CONCLUIDA);
        MaintenanceRequest updatedRequest = maintenanceRequestService.save(request);

        maintenanceHistoryService.save(MaintenanceRequestHistory.builder()
            .maintenanceRequest(updatedRequest)
            .status(RequestStatus.CONCLUIDA)
            .action("Solicitação concluida")
            .actionDate(new Date(System.currentTimeMillis()))
            .employee(employee)
            .build());

        return ResponseEntity.ok().body("Solicitação concluída");
    }

    @PutMapping("/requests/{requestId}/maintain")
    public ResponseEntity<String> doMaintenance(@PathVariable Long requestId, @RequestBody MaintainDTO maintainDTO) {
        
        MaintenanceRequest request = maintenanceRequestService.findById(requestId);

        if (request.getStatus() != RequestStatus.APROVADA) {
            return ResponseEntity.badRequest().body("Serviço não foi aprovado");
        }

        Employee employee = employeeService.employee(maintainDTO.getEmployeeId());

        request.setMaintenanceDescription(maintainDTO.getMaintenanceDescription());
        request.setCustomerOrientation(maintainDTO.getCustomerOrientation());
        request.setStatus(RequestStatus.ARRUMADA);
        MaintenanceRequest updatedRequest = maintenanceRequestService.save(request);

        maintenanceHistoryService.save(MaintenanceRequestHistory.builder()
            .maintenanceRequest(updatedRequest)
            .status(RequestStatus.ARRUMADA)
            .action("Arrumada")
            .actionDate(new Date(System.currentTimeMillis()))
            .employee(employee)
            .build());

        return ResponseEntity.ok().body("Manutenção concluída");
    }

    @PutMapping("/requests/{requestId}/redirect")
    public ResponseEntity<String> redirectMaintenance(@PathVariable Long requestId, @RequestBody RedirectDTO dto) {
        
        if(dto.getEmployeeToRedirectId() == dto.getEmployeeId())
            return ResponseEntity.badRequest().body("Não pode redirecionar a si");

        MaintenanceRequest request = maintenanceRequestService.findById(requestId);

        Employee employee = employeeService.employee(dto.getEmployeeId());
        Employee employeeToRedirect = employeeService.employee(dto.getEmployeeToRedirectId());

        request.setEmployeeId(employeeToRedirect != null ? employeeToRedirect.getId() : null);
        request.setStatus(RequestStatus.REDIRECIONADA);
        MaintenanceRequest updatedRequest = maintenanceRequestService.save(request);

        maintenanceHistoryService.save(MaintenanceRequestHistory.builder()
            .maintenanceRequest(updatedRequest)
            .status(RequestStatus.REDIRECIONADA)
            .action("Solicitação redirecionada")
            .actionDate(new Date(System.currentTimeMillis()))
            .employee(employee)
            .build());

        return ResponseEntity.ok().body("Solicitação redirecionada");
    }

    @GetMapping("/requests/filtered")
    public List<FilteredRequestDTO> getFilteredRequests(
        @RequestParam(defaultValue = "TODAS") String filterType,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam Long employeeId
    ) {
        List<MaintenanceRequest> filteredRequests = maintenanceRequestService.findFilteredRequests(
            filterType, startDate, endDate, employeeId
        );

        return filteredRequests.stream()
            .map(request -> {
                Customer customer = customerService.customer(request.getCustomer().getId());

                return FilteredRequestDTO.builder()
                    .id(request.getId())
                    .registryDate(request.getRegistryDate())
                    .status(request.getStatus())
                    .customerName(customer.getName())
                    .equipmentDescription(request.getEquipmentDescription())
                    .build();
            })
            .collect(Collectors.toList());
    }

    @GetMapping("/revenue/category")
    public List<Map<String, Object>> getRevenueByEquipmentCategory() {
        return maintenanceRequestService.calculateRevenueByEquipmentCategory();
    }

    @GetMapping("/revenue/daily")
    public List<Map<String, Object>> getDailyRevenue(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
    return maintenanceRequestService.calculateRevenueByDay(startDate, endDate);
}

    @GetMapping("/requests/history")
    public List<MaintenanceHistoryDTO> getRequestHistoryByCustomerId(@RequestParam Long customerId) {
        List<MaintenanceRequest> customerRequests = maintenanceRequestService.findRequestsByUserId(customerId);
        
        return customerRequests.stream()
            .flatMap(request -> maintenanceHistoryService.findByRequestId(request.getId()).stream())
            .map(history -> MaintenanceHistoryDTO.builder()
                .id(history.getId())
                .requestId(history.getMaintenanceRequest().getId())
                .status(history.getStatus())
                .action(history.getAction())
                .employeeId(history.getEmployee() != null ? history.getEmployee().getId() : -1)
                .actionDate(history.getActionDate())
                .employeeDTO(history.getEmployee() != null ? employeeService.getEmployee(history.getEmployee().getId()) : null)
                .build())
            .collect(Collectors.toList());
    }

}
