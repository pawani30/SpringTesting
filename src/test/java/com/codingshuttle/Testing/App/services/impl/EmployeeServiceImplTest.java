package com.codingshuttle.Testing.App.services.impl;

import com.codingshuttle.Testing.App.TestContainerConfiguration;
import com.codingshuttle.Testing.App.services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

//------------Testing on Employee Service-----------

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //here we are saying that you should not replace the database
@Import(TestContainerConfiguration.class) //And you should the database that is provided by the testContainer configuration,Now we will not get entityManager error(We wll not get dataJpa related error).It is running , it is using dataJpaTest but still we got some exception and this time it is saying that [ERROR : ]
class EmployeeServiceImplTest {

@Autowired //If we add Autowired here than we need to add @Configuration also and since we are doing unitTesting so we should not add the @SpringBootTest dependency  here because this will run every thing (Run the entire Spring application and load the application context as well) and currently we are only interested in the service code so add @DataJpaTest
//Since we are testing on the employeeService , we are going to hava an object of EmployeeService
private EmployeeService employeeService;

//Methods to be tested inside the employeeService(employeeService has methods like : 1.getEmployeeById 2.createNewEmployee 3..updateEmployee...) and for all these 4 methods we can create multiple testing methods
//In getEmployeeBYId method when you pass in an Id it will return an employee with this particular id and if the employee with that particular id is not found than it will throw an exception,thi sis the expected behaviour of this method
//In this method we are testing for the case when the valid id is given and an employee with that id is returned
//TO make it a JUintTest use @Test from junit
//Since we have to test the Service layer , so we will call the actual methods present inside the employeeService

    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto() {
        employeeService.getEmployeeById(1L);
    }

//TEST CASE FAILED :
//When we run it we will see that the test case is failing Why? Whether it is not able to import the employeeService.We are getting an exception which is saying
//ERROR : Not able to create entityManager Factory   [org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in class path resource ]
//This error occurs when the hibernate is not able to connect with the data JPA that is fine because currently we are not using any embedded(in memory) database , we are using TestContainer Configuration,so we will have to import TestContainerConfiguration
}