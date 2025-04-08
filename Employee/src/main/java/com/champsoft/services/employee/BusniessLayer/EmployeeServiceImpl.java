package com.champsoft.services.employee.BusniessLayer;

import com.champsoft.services.employee.DataLayer.Employee;
import com.champsoft.services.employee.DataLayer.EmployeeRepository;
import com.champsoft.services.employee.MapperLayer.EmployeeRequestMapper;
import com.champsoft.services.employee.MapperLayer.EmployeeResponseMapper;
import com.champsoft.services.employee.PresentationLayer.EmployeeRequestModel;
import com.champsoft.services.employee.PresentationLayer.EmployeeResponseModel;
import com.champsoft.services.utils.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeResponseMapper employeeResponseMapper;
    private final EmployeeRequestMapper employeeRequestMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeResponseMapper employeeResponseMapper,
                               EmployeeRequestMapper employeeRequestMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeResponseMapper = employeeResponseMapper;
        this.employeeRequestMapper = employeeRequestMapper;
    }

    @Override
    public List<EmployeeResponseModel> getAllEmployees() {
        return employeeResponseMapper.entityListToResponseModelList(employeeRepository.findAll());
    }

    @Override
    public EmployeeResponseModel getEmployeeById(String employeeIdentifier) {
        log.info("Fetching employee with ID: {}", employeeIdentifier);
        Optional<Employee> employeeOpt = employeeRepository.findByEmployeeIdentifier(employeeIdentifier);
        if (employeeOpt.isEmpty()) {
            log.error("employee with ID {} not found.", employeeIdentifier);
            throw new NotFoundException("employee with ID " + employeeIdentifier + " not found.");
        }
        return employeeResponseMapper.entityToResponseModel(employeeOpt.get());
    }

    @Override
    public EmployeeResponseModel addEmployee(EmployeeRequestModel employeeRequest) {
        Employee employee = employeeRequestMapper.requestModelToEntity(employeeRequest);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeResponseMapper.entityToResponseModel(savedEmployee);
    }

    @Override
    public EmployeeResponseModel updateEmployee(String employeeId, EmployeeRequestModel updatedEmployee) {
        Optional<Employee> existingEmployeeOpt = employeeRepository.findByEmployeeIdentifier(employeeId);
        if (existingEmployeeOpt.isEmpty()) {
            log.error("employee with ID {} not found.", employeeId);
            throw new NotFoundException("employee with ID " + employeeId + " not found.");
        }

        Employee existingEmployee = existingEmployeeOpt.get();
        Employee employee = employeeRequestMapper.requestModelToEntity(updatedEmployee);
        employee.setId(existingEmployee.getId());
        employee.setEmployeeIdentifier(employeeId);

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeResponseMapper.entityToResponseModel(savedEmployee);
    }

    @Override
    public String deleteEmployee(String employeeId) {
        Optional<Employee> employeeOpt = employeeRepository.findByEmployeeIdentifier(employeeId);
        if (employeeOpt.isEmpty()) {
            log.error("employee with ID {} not found.", employeeId);
            throw new NotFoundException("employee with ID " + employeeId + " not found.");
        }
        employeeRepository.delete(employeeOpt.get());
        return "employee with ID " + employeeId + " deleted successfully.";
    }
}
