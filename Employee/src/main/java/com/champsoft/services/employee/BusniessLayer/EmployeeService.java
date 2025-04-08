package com.champsoft.services.employee.BusniessLayer;

import com.champsoft.services.employee.PresentationLayer.EmployeeRequestModel;
import com.champsoft.services.employee.PresentationLayer.EmployeeResponseModel;

import java.util.List;

public interface EmployeeService {
    List<EmployeeResponseModel> getAllEmployees();

    EmployeeResponseModel getEmployeeById(String employeeId);

    EmployeeResponseModel addEmployee(EmployeeRequestModel employee);

    EmployeeResponseModel updateEmployee(String employeeId, EmployeeRequestModel employee);

    String deleteEmployee(String employeeId);
}
