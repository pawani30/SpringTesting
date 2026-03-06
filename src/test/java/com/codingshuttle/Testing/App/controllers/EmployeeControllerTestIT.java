package com.codingshuttle.Testing.App.controllers;

import com.codingshuttle.Testing.App.TestContainerConfiguration;
import com.codingshuttle.Testing.App.dto.EmployeeDto;
import com.codingshuttle.Testing.App.entities.Employee;
import com.codingshuttle.Testing.App.repositories.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebTestClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//IT = Integration Testing
//Since this is an Integration test we want to so @SpringBootTest here , so that the spring Application could be started and the application context(IOC container) can be loaded.

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//using webEnvironment -> This will actually run the server(real server) and now webTestClient will be autoconfigured and now the bean would be available and we will not get any exception/error in creating the webClient bean and the test will pass and you will see that :
//The tomcat server has been started and a random port no has also been assigned to it which means that it has started a real server on that random port and the server is actually listening for the http requests on that random port no  [Tomcat started on port 62876 (http) with context path '/']
//This will run the whole server and the whole Application Context will be there so that we can make an API call

@AutoConfigureWebTestClient(timeout = "100000") //100sec
//This will autoconfigure the WebTestClient for us,so that we can use the WebTestClient here by autowiring it
//Inside AutoConfigurationWebTestClient,we also want to specify the timeout , here the timeout is very low,iot is actually 5sec by default but we want to specify a bigger number and lets starting the controller

@Import(TestContainerConfiguration.class)
//Also we dont want to use the h2 database(in-memory db),to do that we have to put this line as well
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //That we dont want to replace any db(We dont want to replace testContainer db).Actually we are not using @DataJpaTest annotation here (which do not start the spring boot application,used specially when we want to repository code,loads just the repository related beans and not all the beans and uses the in memory db) and since we are not using that(@DataJpaTest) annotation so we dont need to write this line as well,not necessary because it will now automatically pick up this TestContainerConfiguration now
//It was the habit of Spring @DataJpaTest that it was picking the default h2 database but now we dont need this line here and run it and this will configure the database and it will run the docker database and then it will work on that database only OUTPUT : Server Version: 29.2.0
//  API Version: 1.53
//  Operating System: Docker Desktop
//  Total Memory: 7795 MB
// While running the application,the connection with the docker container is successfully formed but you will see an error like
//ERROR : Error creating bean with name 'com.codingshuttle.Testing.App.controllers.EmployeeControllerTestIT': Unsatisfied dependency expressed through field 'webTestClient' which says that they are not able to create the WebTestClient and the WebTestClient bean is not found
//The WebTestClient bean is only created when we are dealing with the real server and currently we are not running a real server this is actually a mock server which is created by @SpringBootTest and if you want to run a real HTTP server(Tomcat server) we will have to define the web environment property inside @SpringBootTest and we have to define what is the port that you are going to use for this webTestEnvironment we can specify a dedicated port or may be we can generate a random port.Go inside [@SpringBootTest annotation] and there you will see a WebEnvironment Property which needs a WebEnvironment and the default one is MOCK,but we can also define the random port here (Go inside WebEnvironment)
//DEFINING THE RANDOM PORT : It will actually go our server where the code is actually being hosted and being tested and it will find a list of available ports that can be used/are available ports and then it will assign a random port from there

class EmployeeControllerTestIT {

    @Autowired
    private WebTestClient webTestClient; //@AutoConfigureWebTestClient will provide the instance for WebTestClient and we can use it to make HTTP calls and to test upon it
// Also,we dont want to use a real db here.Inside the unit testing of our Persistence layer(repo) we used testContainers.TestContainers are useful when we want to mimic a real db,real prod db and we want to test upon that database only .So we are going to import TestContainer here as well.We have already configured that inside the file TestContainerConfiguration

//We will use the repository here so that we can create some employee first inside the db and then we will try to get that employee with the help of controller method which is getEmployeeById
    @Autowired
    private EmployeeRepository employeeRepository;
//We are going to use the EmployeeRepository first to save the employee for that we will hev to first create an Employee

//CREATING AN EMPLOYEE
    private Employee testEmployee;

    private EmployeeDto testEmployeeDto;


    @BeforeEach //so that this method(of creating) employee can run before/in the starting of running the new test case/Run before every test case
    void setUp() {
        testEmployee = Employee.builder()
                //.id(1L) now the test case will run when we do not manually generate the id
                .email("pawani@gmail.com")
                .name("Pawani")
                .salary(200L)
                .build();

        testEmployeeDto = EmployeeDto.builder()
               // .id(1L)
                .email("pawani@gmail.com")
                .name("Pawani")
                .salary(200L)
                .build();
        employeeRepository.deleteAll();//This method will keep deleting the employeeRepository so that we get a clean db in every test case
    }

//Testing the getEmployeeById method of the controller and first test the happy case
    @Test
    void testGetEmployeeById_success() {
//Create an employee inside the db and the save method is going to give the saved employee
    Employee savedEmployee = employeeRepository.save(testEmployee);
  //testEmployeeDto.setId(1L);
    //savedEmployee.setId(1L); do not work
//Now that we have a savedEmployee(employee saved inside the db) we can use the employee to call the getEmployeeById method.Remember, we are running the real server and we are going to make a real http call and we are going to use WebTestClient to do that.Just like the WebClient , we jave all crud related operations for WebTestClient also put(),post(),delete(),get()
    webTestClient.get()
            .uri("/employees/{id}",savedEmployee.getId()) //here we can define the uri,where do we want to test it.WebTestClient is already configured for our server so we dont have to define the server path here like the localhost:8080(port on which server is running) we dont have to define all that(localhost,port 8080 on which the server is running or any other configurations related to the server(tomcat server)),localhost,port we dont have to define all that.We can just define the uri here.The base path(path for the server like on which server the http request is to be sent/on which port the server is present /actively listening for the HTTP request,we dont have to define that base path).The base path has been defined automatically and we just have to define the uri and the uri is employees and we after that we have to pass in an id here because we are defining the test case for : getEmployeeById method ,so we have to specify the path for that method -./employees/{id}
            .exchange()//This exchange method will actually call the API and this will actually return us the response,this(exchange) will take all the request and then this will make the API call ,so now the API call has been made to the server on the uri and now we have the response so we can do .expectStatus
            .expectStatus().isOk() //These all expect methods are actually the assert methods and we can use assert on these
            //.expectBody(EmployeeDto.class) //Now we have this body,now we can check that if this body is equal to something or not
//jackson is working here to convert the data to employeeDto ,in whichever form the data is returned from the method getEmployeeByid it was converting that data to employeeDto type .It converted the data to employeeDto
           // .isEqualTo(testEmployeeDto); //To which we want to compare the EmployeeDto that is being returned  from the getEmployeeById of the controller class

            .expectBody()
            .jsonPath("$.id").isEqualTo(savedEmployee.getId())
//Here we first try to create an employee because it is a testGetEmployeeById , for that we have to have an employee inside the db.So first we created an employee and then we are trying to call the Api this time to get that employee.If the employee that we are getting from this API(by calling the method of the employee Service) has the same id as the savedEmployee(employee that we created earlier),then the test case is running properly and now all the test cases will work fine
            .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());


//            .value(employeeDto -> {
//                assertThat(employeeDto.getEmail()).isEqualTo(savedEmployee.getEmail());//import statically from Assertions.assertThat coming from assertj by removing Assertions.We can assert upon multiple things here
//                assertThat(employeeDto.getId()).isEqualTo(savedEmployee.getId());
////here, we can individually test upon different different fields or if we dont want to test upon different fields and if you want to test upon the whole thing together than we will have to make sure that the hashCode and equals method is like we want inside the testEmployeeDto(Inside EmployeeDto,we can see the hashCode and the equals method is actually defined on all the fields so this is actually testing upon all the fields present inside the DTO and if all the fields are equal of both the objects because tested(employeeDto,savedEmployee) only in that case the employeeDto is getting us true).After starting it,it will first start the server and than run the test so it is running the real hibernate method ,hibernate is generating queries where it is actually writing query to fetch the employee from the test-container db where id is ... and also doing an insert call also to save the employee first inside the db and after that it is doing a getEmployeeById(for fetching the user by id already present inside the db)
//            });


//use this method(isEqualTo) if you know this testEmployeeDto method (EmployeeDto) hashcode and equals is something that you want to test/you have defined that in which case/what would be the condition for the testEmployeeDto of EmployeeDto class type and the dto returned from the getEmployeeById(which is also of EmployeeDto.class type) you consider these two objects to be equal.Use this method isEqualTo only in the case when if you know EmployeeDto method hashcode and equals is something that you want to test.Otherwise,you can test on multiple other fields as well using .value()
// employeeDto is the Dto that is being returned from the getEmployeeById() method /the dto which is fetched from the db with teh help of id passed while calling the API for getting an employee by id from the db
// savedEmployee is the value for the employee Entity that is actually being saved inside the db which is of type Employee Entity


//OUTPUT : ERROR :Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): This error is coming because we are manually setting the id for the testEmployee and when hibernate tries to save the employee it sees that the user has manually set the id,due to its by default behaviour the hibernate will not generate the insert query to save the employee inside the db,since we have manually set the id,the hibernate will run update command and which running update command when hibernate tries to get/fetch the employee from the db eith that id it does not find that employee in the db because db is empty and we have not inserted any user inside the db till now,we want hibernate to insert the user with id 1 ,but since we are manually setting the id so due to hibernate by default behaviour,the hibernate does not generate insert query ,it generated update query instead and while updating the employee when hibernate tries to fetch the employee with the id 1L it does not find any employee with that id 1L as db is empty and thats why hibernate throws this error  https://chatgpt.com/share/69a9efa9-97c0-8012-9736-77812d99f247
// So we dont have to generate the id manually while creating the testEmployee and let hibernate generate the id by itself because in Entity we have defined the id to be @GeneratedValue(strategy = GENERATIONTYPE.IDENTITY) so hibernate will automatically generate the value
//By default the id generated by hibernate while saving the employee inside the db is also 1 and the employeeDto that we are manually setting id is also 1 thats why  the test case is passing,but if hibernate generates some other id then the test case might fail so it is better that after saving the employee inside the db we explicitly set the id of that savedEmployee to 1L , so that in future the test case dont fail
//NOW INSERT QUERY WILL BE GENERATED BY HIBERNATE BECAUSE WE ARE NOT MANUALLY SETTING THE ID NOW :
//Hibernate:
//    insert
//    into
//        employee
//        (email, name, salary)
//    values
//        (?, ?, ?)

// AND THEN FETCHING THE EMPLOYEE FROM THE DB USING THE SELECT QUERY :  Fetching employee with id : 1
//Hibernate:
//    select
//        e1_0.id,
//        e1_0.email,
//        e1_0.name,
//        e1_0.salary
//    from
//        employee e1_0
//    where
//        e1_0.id=?
//Successfully fetched employee with id : 1

// First,it will start the server and then run the test .So it is running the real hibernate method it is trying to get the employee where id = 1L from the db and also inserting the employee inside the db.We ran the test case with the help of WebTestClent

    }

@Test
//It will give an exception when we try to find an employee by id,that does not exist ,this time we will not save the employee in the db so the db will be empty and when the hibernate will make the query to find the user by id 1L , the user will not be present inside the db and throw an error
    void testGetEmployeeById_Failure() {
   webTestClient.get()
           .uri("/employees/1")
           .exchange() //Calls the actual API and returns the response
           .expectStatus().isNotFound(); //We should not be able to find the employee(We expect it to be the NotFound exception)
//The test will fails and give the exception : ResourceNotFoundException,since the employee with the id 1 is not found inside the db
//OUTPUT : ResourceNotFoundException: Employee not found with id:1
//Expected :404 NOT_FOUND
//Actual   :500 INTERNAL_SERVER_ERROR Do exception handling in the actual code then the code will pass,the exception handling is not being done that is why we are getting 500 Internal Server error and not the 404.After handling the exception,the test case will be passed and since the test case is passed successfully the hibernate will generate a select query to fetch the employee with id specified from the db
//  Hibernate:
//    select
//        e1_0.id,
//        e1_0.email,
//        e1_0.name,
//        e1_0.salary
//    from
//        employee e1_0
//    where
//        e1_0.id=?
//2026-03-06T16:10:17.986+05:30 ERROR 23848 --- [Testing-App] [o-auto-1-exec-1] c.c.T.A.s.impl.EmployeeServiceImpl       : Employee not found with id: 1
    }

//We create a new Employee,if the employee already does not exist inside the db
    @Test
    void testCreateNewEmployee_whenEmployeeAlreadyExists_thenThrowException() {
//First create the employee inside the db as we are testing for employee already exists in the db , so there should be employee inside the db and the db should not be empty
        Employee savedEmployee = employeeRepository.save(testEmployee); //This will create a new employee inside the db.Also we should destroy all the data from db as well after the test case comes to end
// If we dont do / clear the employeeRepository before running a new test case,we will get an exception here only during save method because we are trying to save the employee again in the db.So all the employees will have same email id and in that case if you atre trying to save the same employee again and again in the db without clearing the db before every testcase we would get an exception that you are trying to save an employee with the same email id.So delete all the things as soon as the test is finished

//Calling the Api this is a post mapping (HTTP POST method)
        webTestClient.post()
                .uri("/employees") //Since this is a post request so we have to pass a request body also inside the method
                .bodyValue(EmployeeDto.class)
                .exchange() //Call the Api
                .expectStatus().is5xxServerError();//The status would be internal server error in this case because this is going to result in an error.This is going to a RunTimeException when employee already exists in the db while creating the employee thats why we saved the employee in the db and we are currently not handling the RunTimeException,so we will get 500 error
//  is5xxServerError : is Internal Server error only
// This method is going to save an employee first with some email id and then its trying to save another employee by calling the API/method of employeeService createNewEmployee,by that it wis trying to save one more employee with the same email id.Because the email id that is present inside the testEmployee and the testEmployeeDto is the same and this will result in an internal server error
// OUTPUT : Query generated by hibernate to save the employee first inside the db
// Hibernate:
//    insert
//    into
//        employee
//        (email, name, salary)
//    values
//        (?, ?, ?)
// 2. Trying to fetch the details that whether the user already exists in the db or not,trying to save another employee with the same email id by calling the API/calling the method createnewEmployee
// Hibernate:
//    select
//        e1_0.id,
//        e1_0.email,
//        e1_0.name,
//        e1_0.salary
//    from
//        employee e1_0
//    where
//        e1_0.email=?
//2026-03-06T16:29:52.182+05:30 ERROR 14348 --- [Testing-App] [o-auto-1-exec-4] c.c.T.A.s.impl.EmployeeServiceImpl       : Employee already exists with email : pawani@gmail.com
    }

    @Test
    void testCreateNewEmployee_whenEmployeeDoesNotExists_thenCreateEmployee() {
//This time we will not save the employee in the db because we want that no employee exists with the same email id already and the hibernate created query to insert new user inside the db
        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isCreated() //Status for successful creation is Created
                .expectBody() //here we are not converting the data returned from the method to employeeDto and using the jsonpath method
//We are not going to pass any parameter here inside expectBody() this time and we are going to test upon raw serialized data here,we are not serializing the data and using the json path here
                .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail())
// Jackson will not work in this case as we have not passed inside the expectBody.jackson was working inside testGetEmployeeById_success() because in this method we have passed something inside expectBody that which type of data is expected to be returned from this method call expectBody(EmployeeDto.class)
// Inside jsonpath first define the path ,so this is anyway going to return us an employeeDto because the return type of createNewEmployee method is EmployeeDto only
// Check if the id that we are getting inside the returned value is same as the id here or not,bur since we are creating the employee so id cannot be checked because the id will ve generated by the db
                .jsonPath("$.name").isEqualTo(testEmployeeDto.getName());
// createNewEmployee method task is to create new Employee and then to return that EmployeeDto only and if the email that we are getting is the same email that we have passed than that means it was successfully able to create the employee in the db

// ERROR : Hibernate is generating the insert query for creating a employee inside the db ERROR 4776 --- [Testing-App] [           main] o.s.t.w.reactive.server.ExchangeResult   : Request details for assertion failure:
//
//> POST http://localhost:50298/employees
//> accept-encoding: [gzip]
//> user-agent: [ReactorNetty/1.2.12]
//> host: [localhost:50298]
//> accept: [*/*]
//> WebTestClient-Request-Id: [1]
//> Content-Type: [application/json]
//> Content-Length: [64]
//
//{"id":1,"email":"pawani@gmail.com","name":"Pawani","salary":200}
//
//< 500 INTERNAL_SERVER_ERROR Internal Server Error
//< Content-Length: [0]
//< Date: [Fri, 06 Mar 2026 12:43:38 GMT]
//< Connection: [close]
//
//0 bytes of content (unknown content-type).

// This error means that  -> The request reached the server successfully
//But something failed inside the backend code
//The server did not return any response body, so the test framework printed only the headers
// BECAUSE we are passing the id inside the employeeDto when hibernate tries to create new Employee,inside request body it sees that the id of the employee is already passed so due to its default behaviour if id is already given it do not try to create a new user it tries to update the user so avoid giving the id manually inside the dto as well as entity.After removing the id from the dto,the tesst case will pass successfully

// OUTPUT : Hibernate:
//    insert
//    into
//        employee
//        (email, name, salary)
//    values
//        (?, ?, ?)
//2026-03-06T18:23:39.962+05:30  INFO 8904 --- [Testing-App] [o-auto-1-exec-1] c.c.T.A.s.impl.EmployeeServiceImpl       : Successfully created new employee with id : 1
    }

//
//    @Test
//    void updateEmployee() {
//    }
//
//    @Test
//    void deleteEmployee() {
//    }
}




//By doing shift+f6 you can change all the occurrences of that word in one go


//1.--------------testGetEmployeeById_success--------------
//expectStatus() : This method is going to check if the status that we are getting from this API is one of these or not . We can also define our own staus code here(own values).This one in the getEmployeeById method has to have an ok status basically 200,because we are return ResponseEntity.ok
//To test on the data that we are getting from here.getEmployeeById is going to return an employeeDto and we want to test that if the employee that we are getting from getEmployeeById is equal to the employeeDto(testEmployee) that we created or not.We are finding employee id so the employee that we getting,which is being fetched from the db with the help of the id should be equal to the employee that we have created.For that the emoloyee that is being fetched and returned from the method ,it will be returned in the DTo,first we have to convert it in the Employee Entity type from DTO .We are getting an JSON data from getEmployeeById method(int the form of the DTO,that is being returned to the end user) ,so we can do two things -
//1.We can either convert this data to an Employee for that we can do expectBody and here we have to define the class type, so this will be of type EmployeeDTo.class that will be returned from the method and than we can do asset also
//value() : Here you can get the whole employee,get the entire employeeDTo that is being returned from the function getEmployeeById method inside the controller




//When we run all the test cases together we will get an error Expected :EmployeeDto(id=1, email=pawani@gmail.com, name=Pawani, salary=200)
//Actual   :EmployeeDto(id=2, email=pawani@gmail.com, name=Pawani, salary=200) in the testGetEmployeeById_success() method because there we are trying to see that the savedEmployee is equal to testEmployeeDto or not which we should not do because ids are different because savedEmployee has a separate id and the testEmployeeDto has separate id thats why isEqualTo is failing . In this case we will have to use modelMapper to check / to convert savedEmployee to a new Employee(by converting savedEmployee to employeeDto) or we can check upon multiple other things separately ignoring the id field.because id field will never be the same for the saved employee and for the testEmployeeDto