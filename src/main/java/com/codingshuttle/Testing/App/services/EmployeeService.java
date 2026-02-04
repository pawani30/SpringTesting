package com.codingshuttle.Testing.App.services;

import com.codingshuttle.Testing.App.dto.EmployeeDto;

public interface EmployeeService {


    EmployeeDto getEmployeeById(Long id);

    EmployeeDto createNewEmployee(EmployeeDto employeeDto);


    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);

    void deleteEmployee(Long id);
}
