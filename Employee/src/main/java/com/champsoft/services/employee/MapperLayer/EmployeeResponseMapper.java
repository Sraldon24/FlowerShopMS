package com.champsoft.services.employee.MapperLayer;

import com.champsoft.services.employee.DataLayer.Employee;
import com.champsoft.services.employee.PresentationLayer.EmployeeResponseModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeResponseMapper {
    EmployeeResponseModel entityToResponseModel(Employee employee);

    List<EmployeeResponseModel> entityListToResponseModelList(List<Employee> employees);
}
