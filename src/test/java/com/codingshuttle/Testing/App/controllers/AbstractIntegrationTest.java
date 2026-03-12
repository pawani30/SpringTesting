package com.codingshuttle.Testing.App.controllers;

import com.codingshuttle.Testing.App.TestContainerConfiguration;
import com.codingshuttle.Testing.App.dto.EmployeeDto;
import com.codingshuttle.Testing.App.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000") //100sec

@Import(TestContainerConfiguration.class)
public class AbstractIntegrationTest {

    @Autowired
   //private WebTestClient webTestClient;
     WebTestClient webTestClient; //To make WebTestClient accessible in EmployeeControllerTestIT, make it public
//We can make it public as well as default type so that it is accessible in different file within the same package
// Parent class annotations will be automatically loaded inside the child class

//    private Employee testEmployee;
//    private EmployeeDto testEmployeeDto;

  Employee testEmployee = Employee.builder()
          .email("pawani@gmail.com")
          .name("Pawani")
          .salary(200L)
          //.id((long) 1L)
         .build();

   EmployeeDto testEmployeeDto = EmployeeDto.builder()
            //.id(1L)
            .email("pawani@gmail.com")
                .name("Pawani")
                .salary(200L)
                .build();

}
