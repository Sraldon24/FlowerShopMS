package com.champsoft.services.employee.PresentationLayer;

import com.champsoft.services.employee.BusniessLayer.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping()
    public ResponseEntity<List<EmployeeResponseModel>> getAllEmployees() {
        log.info("Fetching all employees");
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{employeeIdentifier}")
    public ResponseEntity<EmployeeResponseModel> getEmployeeById(@PathVariable String employeeIdentifier) {
        log.info("Fetching employee with ID: {}", employeeIdentifier);
        return ResponseEntity.ok(employeeService.getEmployeeById(employeeIdentifier));
    }

    @PostMapping()
    public ResponseEntity<EmployeeResponseModel> addEmployee(@RequestBody EmployeeRequestModel employeeRequest) {
        log.info("Adding new employee: {}", employeeRequest);
        return ResponseEntity.ok(employeeService.addEmployee(employeeRequest));
    }

    @PutMapping("/{employeeIdentifier}")
    public ResponseEntity<EmployeeResponseModel> updateEmployee(@PathVariable String employeeIdentifier,
                                                                @RequestBody EmployeeRequestModel employeeRequest) {
        log.info("Updating employee with ID: {}", employeeIdentifier);
        return ResponseEntity.ok(employeeService.updateEmployee(employeeIdentifier, employeeRequest));
    }

    @DeleteMapping("/{employeeIdentifier}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String employeeIdentifier) {
        log.info("Deleting employee with ID: {}", employeeIdentifier);
        return ResponseEntity.ok(employeeService.deleteEmployee(employeeIdentifier));
    }
}
