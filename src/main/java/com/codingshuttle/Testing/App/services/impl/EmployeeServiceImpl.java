package com.codingshuttle.Testing.App.services.impl;


import com.codingshuttle.Testing.App.dto.EmployeeDto;
import com.codingshuttle.Testing.App.entities.Employee;
import com.codingshuttle.Testing.App.exceptions.ResourceNotFoundException;
import com.codingshuttle.Testing.App.repositories.EmployeeRepository;
import com.codingshuttle.Testing.App.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Override
    public EmployeeDto getEmployeeById(Long id){
        log.info("Fetching employee with id : {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}",+id);
                    return new ResourceNotFoundException("Employee not found with id:"+id);
                });
        log.info("Successfully fetched employee with id : {}",+id);
       return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public EmployeeDto createNewEmployee(EmployeeDto employeeDto) {
        log.info("Creating new employee with email : {}",employeeDto.getEmail());
        List<Employee> existingEmployees = employeeRepository.findByEmail(employeeDto.getEmail());

        if(!existingEmployees.isEmpty()) { //This line is a false hit
            log.error("Employee already exists with email : {}",employeeDto.getEmail());
            throw new RuntimeException("Employee already exists with email: "+employeeDto.getEmail());
        }
        Employee newEmployee = modelMapper.map(employeeDto, Employee.class);
        Employee savedEmployee = employeeRepository.save(newEmployee);
        log.info("Successfully created new employee with id : {}",savedEmployee.getId());
        return modelMapper.map(savedEmployee, EmployeeDto.class);
    }

    @Override
    public EmployeeDto updateEmployee(Long id,EmployeeDto employeeDto) {
        log.info("updating employee with id : {}",id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with id : {}",id);
                    return new ResourceNotFoundException("Employee not found with id: "+ id);
                });
        if(!employee.getEmail().equals(employeeDto.getEmail())) {
            log.error("Attempted to update email for employee with id : {}",id);
            throw new RuntimeException("The email of the employee cannot be updated");
        }

        //employeeDto.setId(null);
        //modelMapper.map(employeeDto,employee); If we do this than even if we set id to null while mapping modelMapper will not allow to set the id to null and the id will not be set to null and will not give any error while running the test case but when we map it to Employee.class than we will get error and id will remain null it will not be overridden by the modelMapper and now error will come while running the test case
       // modelMapper.map(employeeDto,Employee.class); wrong save it to employee back otherwise the test case will fail
        employee = modelMapper.map(employeeDto, Employee.class);
        employee.setId(id); //here we can modify the id once again once all the fields from the employeeDto are added here employee and now the test case should run successfully

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Successfully updated employee with id : {}",id);
        return modelMapper.map(employee,EmployeeDto.class);

// Learn more on : https://chatgpt.com/share/697fb878-a05c-8012-8812-3f9f6c1731a6
    }

    @Override
    public void deleteEmployee(Long id) {
//        Employee employee = employeeRepository.findById(id)
//                .orElseThrow(() -> {
//                   log.error("Employee with this id not found in the database: {}",id);
//                   throw new ResourceNotFoundException("Employee not found with id"+id);
//                });
// Else employee is found delete the employee
        log.info("Deleting employee with id : {}",id);
        boolean exists = employeeRepository.existsById(id);
        if(!exists) {
            log.error("Employee not found with id : {}",id);
            throw new ResourceNotFoundException("Employee not found with id: "+id);
        }

        employeeRepository.deleteById(id);
        log.info("Successfully deleted employee with id: {}",id);
//        return modelMapper.map(employee,EmployeeDto.class);
    }
}
