package com.codingshuttle.Testing.App.services.impl;

import com.codingshuttle.Testing.App.TestContainerConfiguration;
import com.codingshuttle.Testing.App.dto.EmployeeDto;
import com.codingshuttle.Testing.App.entities.Employee;
import com.codingshuttle.Testing.App.exceptions.ResourceNotFoundException;
import com.codingshuttle.Testing.App.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainerConfiguration.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;


// Inject a mock
      @InjectMocks
      private EmployeeServiceImpl employeeService;


    private Employee  mockEmployee;
    //now same mockEmployee can be used everywhere inside every method so we dont have to create it separately inside every method
    //Long id = 1L;
    private EmployeeDto mockEmployeeDto;
    @BeforeEach
    void setUp() {

         mockEmployee = Employee.builder()
                .id(1L)
                .email("pawani@gmail.com")
                .name("Pawani")
                .salary(200L)
                .build();

        mockEmployeeDto = modelMapper.map(mockEmployee,EmployeeDto.class);
    }

//CREATING EMPLOYEE DTO

    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto() {
  Long id = mockEmployee.getId();

// assign
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee)); //stubbbing

//2.act : The Service method is getEmployeeById
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());

verify(employeeRepository).findById(id);

verify(employeeRepository,atMost(5)).findById(id); //method called maximum 5 times or less
verify(employeeRepository,only()).findById(id);
    }

// Test getEmployeeById where we get an exception
// We are going to test when we are not going to get the employee by id
    @Test
    void testGetEmployeeById_whenEmployeeIsNotPresent_thenThrowException() {
//  arrange : We are going to arrange the method employeeRepository.findById.We are going to mock this method in such a way that we get an exception.When we do not get the Employee by ID in that case this method is going to throw an exception
//To find By any Id we should not get an employee thats why we are using anyLong(),that any id is passed irrespective of whether the employee with that id employee is present or not you should always return null so that it throws an exception
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty()); //This will return an Optional of empty because findById returns an Optional ,now empty will be returned and the exception part of this mehod getEmployeeById present in employeeService will run and we will get resource not found exception already present inside employeeService,we have defined that if the employee is not found by id than throw this exception
//  act
//        employeeService.getEmployeeById(mockEmployee.getId());
//  act and assert
        assertThatThrownBy(() -> employeeService.getEmployeeById(1L)) //once the exception has been thrown we can check that whether this exception is an instance of resource not found exception
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id:1");
// We will also verify that if the findById was called once or not since we are mocking on this method of employee Repository
        verify(employeeRepository).findById(1L);
// Run this method and check if test case is passing or not and the test case will be passed and we will see that the exception part of code inside the findEmployeeById method of employeeService will become green and coverage rate will be increased
    }

    @Test
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployee() {

// Assign
    when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
    when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);

// Act
        EmployeeDto employeeDto = employeeService.createNewEmployee(mockEmployeeDto);

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());

        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);

        verify(employeeRepository).save(employeeArgumentCaptor.capture());

//Getting the value
Employee capturedEmployee = employeeArgumentCaptor.getValue();
assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());
    }

// Failing case for createNewEmployee : When employee List is not empty than we will get an exception because the employee is already present and this list will be not empty if we try to find employee by email and we should not create an employee whose email is already there in the db
    @Test
    void testCreateNewEmployee_whenAttemptingToCreateEmployeeWithExistingEmail_thenThrowException() {
//Arrange
// We should not be able to create an employee whose already there inside the system in that case throw an exception
// First we have to create a new employee and than we have to check that if this employee is present or not but we are not going to use employee Repository to do that because this is a unit test , so we are going to mock the behaviour that when the employeeRepository.findByEmail() is going to be called in that case we should return a non empty list of employee so that the code goes into an exception

//        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of(mockEmployee));
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee)); //only in this particular case ,you should fail this particular test case now (more particular)
//here,we are passing a non empty list while earlier we have passed an empty list so thats why we were able to by pass this if condition but this time we are going to pass an employee(non empty list) and the function will go inside the if condition and return an exception

//Act and assert
        assertThatThrownBy(() -> employeeService.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class) //This has to be an instance of runTimeException
                .hasMessage("Employee already exists with email: "+mockEmployee.getEmail());

//  verify that the findByEmail was called only once
//    verify(employeeRepository).findByEmail(anyString());
        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
// Instead of anyString we can also pass in the string that is passed here inside this method invocation.We are calling the method createNewEmployee and we are passing mockEmployeeDto inside that and we can also do mockEmployee.getEmail() instead of anyString(),so this will be more precise now it is saying that only in this case you should fail this test case
// Also verify that the employee repository never calls the save method because since it is failing it should not call the save method
        verify(employeeRepository,never()).save(any()); //This should not be called by anything
// Run this method and it will pass the test case  OUTPUT : Creating new employee with email : pawani@gmail.com
// Employee already exists with email : pawani@gmail.com
    }


//FOR UPDATE : handling failing cases first
    @Test
void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException() {
        //arrange : employeeRepository.findById should not return any employee
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
// We are saying that when this method is called with this input than you should return an empty employee and in that case it will throw an exception

// Act and assert ->updated employee we are passing as mockEmployeeDto inside the update method
        assertThatThrownBy(() -> employeeService.updateEmployee(1L,mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee not found with id: 1");
        verify(employeeRepository).findById(1L); //verify that findById was called
        verify(employeeRepository,never()).save(any()); //Also verify that the save method was never called
//  OUTPUT : updating employee with id : 1
// Employee not found with id : 1
    }

//We again throw an runTimeException while updating the employee and this happens when we are trying to update email of an employee and the email does not match.Basically we are authenticating the user that this user is allowed to update the employee or not.We are saying that you are not allowed to update the email of the employee and if someone is trying to update the email you should throw an exception which is a runTimeException
    @Test
    void testUpdateEmployee_whenAttemptingToUpdateEmail_thenThrowException() {
// First get an employee
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee)); //return an employee
// And the employee email(mockEmployee email) is same as the email of the DTo(mockEmployeeDto) because we have created both using the same fields here so the mockEmployee has the email of pawani@gmail.com and mockEmployeeDto also has the same email

//Updating the employee : Even if you are trying to update the name say any random name but you are trying to update the email as well
        mockEmployeeDto.setName("Random");
        mockEmployeeDto.setEmail("random@gmail.com"); //you should not be allowed to do so because we have changed the email of the employee and now the email fo DTo and mockEmployee will not match which will result in an exception and we will not be allowed to update the employee

// act and assert
        assertThatThrownBy(() -> employeeService.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto)) //since it is a mock test the ids of both mockEmployeeDto and mockEmployee is ame which is 1L but in real the ids are not at all same because when the employee is stored inside the db the id is autogenerated by the db so the id cant be equal
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");
// Here we are trying to update the employee but we are also trying to update the email of the employee as well .The id is going to be the same the id of the employee that we are going to update is actually 1 (employee fetched from the db)but we are also trying to update the email which is not alllowed

        verify(employeeRepository).findById(mockEmployeeDto.getId()); //verify that findById is being called once with this particular value
        verify(employeeRepository,never()).save(any()); //also verify that the save method was never called on any kind of object
    }

//Update an employee which already exist and we are not trying to throw any exception and we are no trying to update the email of the employee

    @Test
    void testUpdateEmployee_whenValidEmployee_thenUpdateEmployee() {
//arrange
//First find by id
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
//Update the employee -> only update the things that we are allowed to update
        mockEmployeeDto.setName("Random name");
        mockEmployeeDto.setSalary(199L);

// when(employeeRepository.save(mockEmployee)).thenReturn(mockEmployee); Wrong because even after changing the mockEmployeeDto name and salary we are storing the same old mockEmployee where name and salary of the employee is not changed(where name is still pawani)

//Mocking the save method
//get the changed / updated employee(The save employee is going to look like the changed / updated employee where name is Random and salary is 199 because we are going to store updated employee only inside the db)
        Employee newEmployee = modelMapper.map(mockEmployeeDto, Employee.class);
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);
// When you are trying to save by anything just return the newEmployee

//act and assert : Now we can check that the employee that we are getting from save method is actually equal to mockEmployeeDto that the user passed as parameter to which the user was to be updated to that or not
  EmployeeDto updatedEmployeeDto = employeeService.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto);

  assertThat(updatedEmployeeDto).isEqualTo(mockEmployeeDto); //here we are going to check that how this equals method is being implemented inside the mockEmployeeDto.Go to mockEmployeeDto and there we can see that the Equals method is not being implemented so either we can implement this equals method that will specify that on what basis of what (which field) we want to consider the objects as equal
   //Now writing this equal will work because this will check if these two things are equal or not mockEmployeeDto we were passing to the function as parameter to which we want the employee to be updated and updatedEmployee is the object that is actually being saved inside the db
//  If these are equal this means that the unit test case is actually working

        verify(employeeRepository).findById(1L); //Whether the method was called or not
        verify(employeeRepository).save(any()); //save method was called or not and run it


//We will get an error because the mockEmployeeDto that we are passing while calling the updateEmployeeDto method is getting updated inside the employeeService method updateEmployee and is being set to null and we should not set the id to null before converting the dto to employee.class and if you want that
//Since inside the service we are setting the id to null. modelmapper.map->first of all pass in all the values from EmployeeDto to employee and if you want to make sure that the id is not being changed ,the id with which the employee is created inside the db is not being changed which is autogenerated by the db ,by the id that user is passing we can do employee.setId(and the id could be the id that is being passed inside the updateEmployee function as a parameter)
//  OUTPUT : Creating new employee with email : pawani@gmail.com
// Successfully created new employee with id : 1
    }

// Delete Employee : when the employee does not exist
    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExists_thenThrowException() {
        when(employeeRepository.existsById(1L)).thenReturn(false); //here we are calling existsById not findById so it will not return Optional.of(mockEmployee) since existsById returns only true or false

//act
        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: "+1L);

        verify(employeeRepository,never()).deleteById(anyLong()); //Also check that the employeeRepository has never called the deleteById for any Id since we are throwing the exception that will not be called
// OUTPUT : Deleting employee with id : 1
//Employee not found with id :
    }

    @Test
    void testDeleteEmployee_whenEmployeeIsValid_thenDeleteEmployee() {
        //arrange
        when(employeeRepository.existsById(1L)).thenReturn(true);
//assertThat when we call the deleteEmployee method of the EmployeeService so when this method was called it does not throw any exception
        assertThatCode(() -> employeeService.deleteEmployee(1L))
                .doesNotThrowAnyException();
// assertThat when we are not returning anything from the method , we are not returning anything from the deleteById method,so we want to assertThat when this method was called than it didi not assert in any kind of exception and we also want to asser that yes delete By id method was called atleast once

        verify(employeeRepository).deleteById(1L); //Just check that the deleteById method of employee Repository was called for one,since deleteById do not return anything
    }
}

//Coverage tells that how much percentage if overall code are you testing branches mean that we are not touching all the if else conditions all the branches of our code
//The red line that we see mean that we have not tested that code yet and the hit is 0,we have to create test cases such that we are hitting all of the end points
//Run with coverage : The yellow line means that in createNewEmployee there is a if condition that is checking the whether the list is empty or not so that if condition is being checked but is resulting in false and the block inside the if condition is not getting excuted so it is a hit(the if condition is being executed to check whether the list is empty or not) but it is a false hit such that the if condition is resulting in false and the code inside the if condition is not getting executed

//Regression Testing : The code that was previously there is not being affected by the new code that you are putting inside your codebase(for checking : new code is not breaking your older code)