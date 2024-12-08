package ufpr.web.security.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ufpr.web.entities.Employee;
import ufpr.web.entities.MaintenanceRequest;
import ufpr.web.services.EmployeeService;
import ufpr.web.services.MaintenanceRequestService;
import ufpr.web.types.enums.RequestStatus;

@Aspect
@Component
public class EmployeeRequestAccessAspect {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MaintenanceRequestService maintenanceRequestService;

    @Around("@annotation(EmployeeRequestAccess)")
    public Object checkAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Long requestId = null;
        
        // Find requestId in method arguments
        for (Object arg : args) {
            if (arg instanceof Long) {
                requestId = (Long) arg;
                break;
            }
        }

        if (requestId == null) {
            throw new SecurityException("Request ID not found in method arguments");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = employeeService.findByEmail(email);
        MaintenanceRequest request = maintenanceRequestService.findById(requestId);

        if (request.getStatus() == RequestStatus.ABERTA || 
            (request.getEmployeeId() != null && request.getEmployeeId().equals(employee.getId()))) {
            return joinPoint.proceed();
        }

        throw new SecurityException("Employee does not have access to this request");
    }
} 