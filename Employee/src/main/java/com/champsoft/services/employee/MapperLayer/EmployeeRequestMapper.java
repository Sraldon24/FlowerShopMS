package com.champsoft.services.employee.MapperLayer;

import com.champsoft.services.employee.DataLayer.Employee;
import com.champsoft.services.employee.PresentationLayer.EmployeeRequestModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeRequestMapper {
    Employee requestModelToEntity(EmployeeRequestModel employeeRequestModel);
}
