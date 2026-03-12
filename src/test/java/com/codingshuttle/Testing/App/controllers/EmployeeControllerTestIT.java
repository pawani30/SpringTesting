package com.codingshuttle.Testing.App.controllers;

import com.codingshuttle.Testing.App.dto.EmployeeDto;
import com.codingshuttle.Testing.App.entities.Employee;
import com.codingshuttle.Testing.App.repositories.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebTestClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.TimeZone;


class EmployeeControllerTestIT extends AbstractIntegrationTest{

//    @Autowired
//    private WebTestClient webTestClient; //@AutoConfigureWebTestClient will provide the instance for WebTestClient and we can use it to make HTTP calls and to test upon it

    @Autowired
    private EmployeeRepository employeeRepository;

// Before running every test case , we are setting the time zone
    @BeforeAll
    static void setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
    }


    @BeforeEach
    void setUp() {   employeeRepository.deleteAll(); }

    @Test
    void testGetEmployeeById_success() {
    Employee savedEmployee = employeeRepository.save(testEmployee);
    webTestClient.get()
            .uri("/employees/{id}",savedEmployee.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(savedEmployee.getId())
            .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());
    }

@Test
    void testGetEmployeeById_Failure() {
   webTestClient.get()
           .uri("/employees/1")
           .exchange()
           .expectStatus().isNotFound();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeAlreadyExists_thenThrowException() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        webTestClient.post()
                .uri("/employees")
                .bodyValue(EmployeeDto.class)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeDoesNotExists_thenCreateEmployee() {
        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail())
                .jsonPath("$.name").isEqualTo(testEmployeeDto.getName());
    }


    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThroeException () {
        webTestClient.put()
                .uri("/employees/999")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isNotFound();
// We are making the put API call so it should go inside the put (update API path only),make sure to pass the bodyValue in the request
// OUTPUT : Hibernate:
//    select
//        e1_0.id,
//        e1_0.email,
//        e1_0.name,
//        e1_0.salary
//    from
//        employee e1_0
//    where
//        e1_0.id=?
//2026-03-06T19:32:50.889+05:30 ERROR 4844 --- [Testing-App] [o-auto-1-exec-3] c.c.T.A.s.impl.EmployeeServiceImpl       : Employee not found with id : 999

    }
//When user is trying to update email,then also it should throw an exception
    @Test
    void testUpdateEmployee_whenAttemptingToUpdateTheEmail_thenThrowException() {
        Employee savedEmployee = employeeRepository.save(testEmployee); //This employee was not saved in the db by calling any API,this was saved by calling the method of employeeRepository which was already being tested
//This is making sure that we are saving an employee and try to update the same employee that is being saved inside the db

//UPDATING THE FIELDS :
        testEmployeeDto.setName("Random Name");
        testEmployeeDto.setEmail("random@gmail.com");

//TRYing to make a put API call to update the employee -
        webTestClient.put()
                .uri("/employees/{id}",savedEmployee.getId())//trying to update the same employee that we just saved in the db but this time the email has been changed
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError(); //we are expecting to get an error from here so dont do expectBody

//If we get internal Server error , that means that we are not able to update the email of the existing employee and that is exactly what we want
//OUTPUT : Hibernate:
//    select
//        e1_0.id,
//        e1_0.email,
//        e1_0.name,
//        e1_0.salary
//    from
//        employee e1_0
//    where
//        e1_0.id=?
//2026-03-06T19:47:21.362+05:30 ERROR 16356 --- [Testing-App] [o-auto-1-exec-5] c.c.T.A.s.impl.EmployeeServiceImpl       : Attempted to update email for employee with id : 3
    }


    @Test
    void testUpdateEmployee_whenEmployeeIsValid_thenUpdateEmployee() {
        Employee savedEmployee = employeeRepository.save(testEmployee);

        testEmployeeDto.setId(savedEmployee.getId());

//        UPDATING VALUES
        testEmployeeDto.setName("Random name");
        testEmployeeDto.setSalary(250L);

        webTestClient.put()
                .uri("/employees/{id}",savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isOk() //Because the method inside employeeService is returning status ok when everything is goog and the user is updated successfully
                .expectBody(EmployeeDto.class)
//    We can check if the name and the salary we are getting from API call are equal to the testEmployeeDto or not,for that we have to write expectBody
                .isEqualTo(testEmployeeDto);

//  First,it is creating aan employee and then it is changing the employee and then we are trying to update the same employee that is being saved inside the db and then we are checking that the result(updatedEmployee) that we are getting from the API call is equal to the input or not(testEmployeeDto,to which we want the employee to get updated)
// OUTPUT : Hibernate:
//    update
//        employee
//    set
//        email=?,
//        name=?,
//        salary=?
//    where
//        id=?
//2026-03-06T21:28:37.216+05:30  INFO 10944 --- [Testing-App] [o-auto-1-exec-1] c.c.T.A.s.impl.EmployeeServiceImpl       : Successfully updated employee with id : 1
    }


    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExists_thenThrowException() {
// We will not try to save the employee , directly delete the employee because we want to throw an exception.For throwing exception the employee should not be found inside the db and the db should be empty

        webTestClient.delete()
                .uri("/employees/1") //delete API would be called on the id 1
                .exchange()
                .expectStatus().isNotFound(); //Resource Not Found Exception will be thrown with the status of 404

//OUTPUT : Hibernate:
//    delete
//    from
//        employee
//    where
//        id=?
//2026-03-06T21:35:41.467+05:30  INFO 2028 --- [Testing-App] [o-auto-1-exec-7] c.c.T.A.s.impl.EmployeeServiceImpl       : Deleting employee with id : 1
//Hibernate:
//    select
//        count(*)
//    from
//        employee e1_0
//    where
//        e1_0.id=?
//2026-03-06T21:35:41.688+05:30 ERROR 2028 --- [Testing-App] [o-auto-1-exec-7] c.c.T.A.s.impl.EmployeeServiceImpl       : Employee not found with id : 1
    }

    @Test
    void testDeleteEmployee_whenEmployeeExists_thenDeleteEmployee() {
// For this,the employee should be first present inside the db so that  when searching for the employee in the db for deleting,it do not throw any exception
        Employee savedEmployee = employeeRepository.save(testEmployee);

        webTestClient.delete()
                .uri("/employees/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().isNoContent() //because this is what we are returning from the delete method of the employeeService method
                .expectBody(Void.class); //As void is returned from the delete method ResponseEntity<Void>

//Once the employee has been deleted,we are trying to call the same method to delete the employee and now we should exception because we have already deleted the employee with that id from the db
        webTestClient.delete()
                .uri("/employees/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().isNotFound(); //This will  reconfirm that the employee was successfully deleted from the db


//OUTPUT : Hibernate:
//    delete
//    from
//        employee
//    where
//        id=?
//2026-03-06T21:50:19.676+05:30  INFO 3464 --- [Testing-App] [o-auto-1-exec-6] c.c.T.A.s.impl.EmployeeServiceImpl       : Deleting employee with id : 1

//Rechecking that whether the employee was deleted successfully from the db or not :
// Hibernate:
//    select
//        count(*)
//    from
//        employee e1_0
//    where
//        e1_0.id=?
//2026-03-06T21:50:19.834+05:30 ERROR 3464 --- [Testing-App] [o-auto-1-exec-6] c.c.T.A.s.impl.EmployeeServiceImpl       : Employee not found with id : 1
    }
}


//We have written this integration testing just for one Controller(EmployeeController) but we can have tones of controller classes inside the appplication and we dont want to repeat the same things , same configuration lines again and again for all the controllers(For importing test containers , or the basic annotations) we can optimize it . Inside the controllers only,we can make a abstract class that can be used to extend our different classes



