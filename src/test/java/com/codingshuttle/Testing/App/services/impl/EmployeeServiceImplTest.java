package com.codingshuttle.Testing.App.services.impl;

import com.codingshuttle.Testing.App.TestContainerConfiguration;
import com.codingshuttle.Testing.App.dto.EmployeeDto;
import com.codingshuttle.Testing.App.entities.Employee;
import com.codingshuttle.Testing.App.repositories.EmployeeRepository;
import com.codingshuttle.Testing.App.services.EmployeeService;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//------------Testing on Employee Service-----------

//@DataJpaTest
//@SpringBootTest
@ExtendWith(MockitoExtension.class)

//TEST CASE FAILED :
//When we run it we will see that the test case is failing Why? Whether it is not able to import the employeeService.We are getting an exception which is saying
//ERROR : Not able to create entityManager Factory   [org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in class path resource ]
//This error occurs when the hibernate is not able to connect with the data JPA that is fine because currently we are not using any embedded(in memory) database , we are using TestContainer Configuration,so we will have to import TestContainerConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //here we are saying that you should not replace the database
@Import(TestContainerConfiguration.class) //And you should use the database that is provided by the testContainer configuration,Now if run this,we will not get entityManager error(We wll not get dataJpa related error).It is running , it is using dataJpaTest but still we got some exception and this time it is saying that [ERROR : Error creating bean with name 'com.codingshuttle.Testing.App.services.impl.EmployeeServiceImplTest': Unsatisfied dependency expressed through field 'employeeService': No qualifying bean of type 'com.codingshuttle.Testing.App.services.EmployeeService' available: expected at least 1 bean which qualifies as autowire candidate.].
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
// We are going to mock the EmployeeRepository and with the help of @InjectMocks we are also saying that put these mocks(EmployeeRepository) inside this (EmployeeServiceImpl) and if we have any more mocks Ex : if you have lets say modelmapper mock because EmployeeServiceImpl is also dependent on modelMapper as well , so we can put this one as well


// Create a mock
    //@Mock
    @Spy
    private ModelMapper modelMapper;
// And you can put all these(EmployeeRepository and modelmapper) inside this particular class(EmployeeServiceImpl).It will create employeeService and it will put all these Mocks inside this @InjectMocks
// We are mocking employeeRepository and modelMapper.Run this  , before we were getting the error that (because "this.employeeRepository" is null) but by adding the @Mock , now employeeRepository will not be null.We are still getting an error but this time the error is employee not found with id 1 which is expected,but now the EmployeeRepository method findById is actually being called and it is searching for the employee with the id 1 and the employee is not found thats why it is giving the error , this was expected
//WHAT IS EXACTLY HAPPENING BEHIND THE MOCKING ?
//We were able to create an employeeRepository and we are now getting error due to this employeeRepository being null . We are getting an error because this employeeRepository is not null and we are able to call the findById method on our testContainer db(docker image) and there it did not find any employee with id 1 and thats why it is giving this error
// We were able to successfully create employeeService bean and also able to inject the employeeRepository bean inside the employeeService as well with the help of @Mock and @InjectMock
//STUBBING : HOW TO STUB IT. MOCK ITS BEHAVIOUR? we have these three thing when,thenReturn and thenThrow to mock the behaviour
// Inside any type of service related code,inside any type of test related code we have these three things - 1.assign 2.act 3.assert
// In the assign part we assign something



//@Autowired //If we add Autowired here than we need to add @Configuration also and since we are doing unitTesting so we should not add the @SpringBootTest dependency  here because this will run every thing (Run the entire Spring application and load the application context as well) and currently we are only interested in the service code so add @DataJpaTest
//Since we are testing on the employeeService , we are going to have an object of EmployeeService


// Inject a mock
      @InjectMocks
      private EmployeeServiceImpl employeeService;
//       private EmployeeService employeeService;

//Methods to be tested inside the employeeService(employeeService has methods like : 1.getEmployeeById 2.createNewEmployee 3..updateEmployee...) and for all these 4 methods we can create multiple testing methods
//In getEmployeeBYId method when you pass in an Id it will return an employee with this particular id and if the employee with that particular id is not found than it will throw an exception,thi sis the expected behaviour of this method
//In this method we are testing for the case when the valid id is given and an employee with that id is returned
//TO make it a JUintTest use @Test from junit
//Since we have to test the Service layer , so we will call the actual methods present inside the employeeService

//CREATING A GLOBAL METHOD SO THAT IT CAN BE USED BEFORE EACH Test method

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
        //employeeService.getEmployeeById(1L);

  Long id = mockEmployee.getId();

//  Creating a Mock Employee - Create a employee using the employee builder
//        Employee mockEmployee = Employee.builder()
//                .id(id)
//                .email("pawani@gmail.com")
//                .name("Pawani")
//                .salary(200L)
//                .build();


// assign : Whenever this findById is called on the employeeRepository than you should return something.Thats it and thats  how you can stub it
//We are going to assign using when and import this when which will come from mockito only

        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee)); //stubbbing

// when this method (findById) is called we want to stub it with my own object,so whenever you are calling this method findById inside the employeeService,I want you to return this particular thing which is return inside thenReturn
//The find by Id method inside employeeRepository is actually going to return an Optional Employee,so we also want to return an Optional Employee from here.Optional is simply a class in JAVA that handles null value for users and if the answer is null it does not throw nullPointerException and forces the suer to handle the nullPointerException by making it an checked exception so that user has to handle it (null value) otherwise the code will give compile time error and will not be compiled
// To return an Optional Employee,we have to create a mock of this employee as well .
// Now we have the mock employee and we can return this mock Employee inside the thenReturn and we cannot directly return it we have to return an optional because this findById method inside the EmployeeRepository is actually going to return an optional
//This is how we are assigning it and also stubbing mocking the behaviour also
//We are saying that this is the mock that I have created and since this is a mock I have to define its behaviour as well because this is a mock this is not a exact object that is created for the Employee , so I have to assign / define all its behaviour by myself so whenever it is being used inside the class(inside the service class the mock Employee Object) , we have to define its behaviour. Since it is being used inside the fundById method so its behaviour is defined like this : Whenever the Service is calling you like this employeeRepository.findById() you are going to return a mock Employee that we have created here and now we are going to act on this Service method getEmployeeById

//2.act : The Service method is getEmployeeById
        EmployeeDto employeeDto = employeeService.getEmployeeById(id); //This is an actual service because I want to test on an actual Service but I dont want to test on an actual Repository
// Here , we are going to call the getEmployeeById of the employeeService ,this is Service is our Service AND this (when(employeeRepsitory.findById()---------- )this is the injected mocks
//When the actual Service method getEmployeeById,calls the employeeRepository.findbyId method this [when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));] particular mock result will be returned [here -> In the employeeService : Employee employee = employeeRepository.findById(id),which means that the employee with the id 1l with neither be searched for in the testContainer db also which we have formed by copying the actual the production db inside the docker but it will return a mockEmployee that we have created / hardcoded inside this file only which is mockEmployee)]
// So we will get this mockEmployee here(Employee employee = employeeRepository.findById(id)) and than this mock employee will be returned from here return modelMapper.map(employee,EmployeeDto.class),but here we also have to define the mock for the modelMapper .We dont want to define the mock for the modelMapper because this modelMapper is a third party library and this is also supposed to do whatever we are telling it to do (For Ex : I cam map one thins to another thing now we are mapping employee to employeeDto using modelMapper inside the employeeService class now whether that employee comes from the actual production db,or from the testcontainer db(which is a copy of the production db),or whether it is a mock employee inside that employee variable of type Employee mnodelMapper simply has top convert the employee to Dto).modelmapper work is to simply used to map from one thing to another thing,so we dont need to mock the modelMapper
//But if we remove the @Mock on the modelMapper than who is going to supply this modelMapper here because remember the SpringBootApplication is not started and no beans have been registered inside the application Context since it is not created only
//For that we have @Spy to do that  @Spy : We are spying on modelMapper , whicvh means that we are using the actual modelMapper which is present inside the SpringBoot Code that is the real modelMapper we are not actually creating a mock of this,we are actually creating the real modelMapper that is present and this modelMapper is there in the autoConfiguration we have added it here in the config->AppConfig.So use this instance of modelMapper and put it here so this modelMapper would be put inside the employeeService and we are not mocking the modelMapper because this modelMapper is no use for me.This modelMapper is already is being tested so we dont have to mock it

// assert : We can also assert on somethings,we are going to get an employeeDto here and we can say that the employeeDto that I am going to get from here I can check multiple things on it and we have assert for that,make sure to import it statically from assertJ only org.,assertj.core.api
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(id);
// assertThat the id that we are getting from the employeeDto which is the mockEmployee only that mockEmployee id is equal to the id that is present over here
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
// Now the test cases have passed successfully ->Fetching employee with id : 1
// Successfully fetched employee with id : 1 .These we have already specified inside the getEmployeeById from EmployeeService
//  here,I did not interact with the real employee Repository This is the mocked employeeRepository and we are saying that whenever the service is calling this method employeeRepository.findByid , you should return a mockEmployee



// verify methods :
// Check was the findById called or not on the mock object.We re checking that on this particular mock employeeRepository was this method called or not and we are not getting any error while running it that means this method findById on this mock employeeRepository was called once
        verify(employeeRepository).findById(id);
//Check if the save method with null argument was called or not on the mock object
       // verify(employeeRepository).save(null); //here we will get an exception while running it,basically test will get failed and it will say that it wanted employeeRepository to invoke the save method but this was not invoked,however the findById method was invoked on the employeeRepository mocked object
//ERROR : Wanted but not invoked:employeeRepository.save(null);
//Check if the findById method was called for id No : 2 or not
        //verify(employeeRepository).findById(2L); //No findById method was called only for id 1
// ERROR : We wanted to invoke findById by 2 but actually it invoked with findById 1 only when you pass 1 , or may be the id than it will simply work
//        Wanted but not invoked:
//        employeeRepository.findById(2L);
//        However, there was exactly 1 interaction with this mock:
//         employeeRepository.findById(1L);

//  PASSING VERIFICATION MODES inside the verify method : We can also check that how many times this particular method(findById) was called on the mock object employeeRepository
//verify(employeeRepository, times(2)).findById(id);
//OUTPUT : employeeRepository.findById(1L);
//Wanted 2 times:
//-> at com.codingshuttle.Testing.App.services.impl.EmployeeServiceImplTest.testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto(EmployeeServiceImplTest.java:137)
//But was 1 time:

//verify(employeeRepository,atLeast(1)).findById(id); //successfull
verify(employeeRepository,atMost(5)).findById(id); //method called maximum 5 times or less
verify(employeeRepository,only()).findById(id); //it will return true .If this only method was called and no other method was called from the employeeRepository
//It will make sure that only findById method was called and no other method was called on the employeeRepository mock
    }

//Here,testing is done for the happy case only inside the createNewEmployee , here we are saying that if yoy get an createNewEmployee request than simply create that employee
// Inside the createNewEmployee method in the Service,first we are checking that inside the Dto request that we are getting as a parameter ,if the email present inside the employeeDto if the email is already there / exists inside the employee table (in the db) or not.If the employee with this email already exist inside the db than we should not create again the employee and throw the exception that employee already exists with this email,but if not present than save the employee
    @Test
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployee() {
// Assign : Since we are defining employeeRepository as the mock inside the createNewEmployee method,so we have to define all the behaviours of the employeeRepository here.In the createNewEmployee method,employeeRepository is called two(2) times 1st time : To get the employee with this particular email and 2nd time to save the employee inside the db ,so we ahve to define both these behaviours here
// If we want to return for any string that any string if it is passsed doesnt matter for every string we have to return the same thing
//  when(employeeRepository.findByEmail("pawani@gmail.com"))
    when(employeeRepository.findByEmail(anyString())).thenReturn(List.of()); //here we are saying that when you are calling the findByEmail with any string then simply return a empty List of employee .So even if the user with the email already exist in the db,we are mocking the behaviour that whenever findByEmail method on employeeRepository , irrespective of the string/email whether it exist or not inside the db you always have to return an empty List because inside the employeeService we are creating an new employee only when the List is Empty,so here we have to intentionally meke sure that the List is Empty only so that the employee can be saved as we want to test that only that whether the employee is created inside the db or not
//when the employeeRepository.findByEmail is called for any type of string thenReturn ,we are testing for happy case in that case we should not an employee whose email already exist in the db.In that case we should return an empty List of employee because in createNewEmployee we are checking that if the List is not empty only then throw an exception but if the list is empty than it should not throw an exception
    when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);
// Similarly,when the save method is called on any type of employeeEntity for that we can do any and we can also define type of this any
//any(Employee.class) -> When any object of the Employee.class is being saved than you should return the saved employee so whet is the value of this saved employee for that we have to create this saved employee first which is the mockEmployee
//We are saying that whenever you try to findByEmail just return an emptyListofEmployee so that you dont throw an exception and when to try to save an employee just return the mockeEmployee

// Act
//Inside createNewEmployee , we have to pass an EmployeeDto as well we cannot pass mockEmployee because mockEmployee is of type Employee but we want to pass of type EmployeeDto
        EmployeeDto employeeDto = employeeService.createNewEmployee(mockEmployeeDto);

// Assert :
// Here we should be able to create the employee inside the database and than from there we are getting the employeeDto(mockEmployeeDto) we should check that if this employeeDto that we are getting from here by calling createNewEmployee of employeeService is actually equal to mockEmployee or not
//First we have to check that if the employeeDto that is being returned is null or not
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());
// We can check for id also but id will be passed differently.Like the id by which the employee will be stored in the db will be different os while creating the employee in employeeDto the user will not pass the id so we canniot check that the the employee which is stored in the db that emoployee id is equal to the id that we have passed because we are not passing any id while creating the employee
// We can also verify that the save method was called or not of the employeeRepository because we are creating something than save method must be called once
// Verifying that the save method was called for Employee class,Currently we are passing any
        //verify(employeeRepository).save(any(Employee.class));
        //It is checking that if the save method was called for any kind of data or if we have the class here we can pass that also because we should only check for Employee class
    //We are checking that if the save method was called for Employee object or not

// OUTPUT : Creating new employee with email : pawani@gmail.com
// Successfully created new employee with id : 1
// here we are not interested in seeing that if the employee is actually saved in the db or not , we are just checking that if the employeeService is working properly or not.EmployeeService is supposed to save an employee and then first check that if the employee with the same email exists or not,it is doing that and save the employee and convert it to the dto and return back
// EmployeeService is working and we are not interested in checking that repository save method is actually saving the employee in db or not because we have already tested the repository in a separate unit test,here we are only checking that whether employeeService is working or not
// So WHY WE ARE NOT CHECKING THAT THE EMPLOYEE IS ACTUALLY SAVED IN THE DB OR NOT?Because it is the work of employeeeRepository to actually save the employee in the db and not of employeeService and repository is already being unit tested currently we are only interested in seeing that if the service part is working properly or not


        // Argument Captor : This will instantiate an Employee based captor for us that will capture an argument of Employe type
        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);

// Capture the argument : verifying in the save method because we are passing inside the save method the employee that is actually going inside the db to get stored
        verify(employeeRepository).save(employeeArgumentCaptor.capture()); //It will actually capture the real object that was passed over here (Inside employeeService , Employee employee = employeeRepository.save(newEmployee)),so it is this object newEmployee Now we can get hold of this object and we can also assert on newEmployee this object that what is the value of this object
//here also we have mocked the save method and stubbed its behaviour that whenever the save method will called always return the mockEmployee but we dont have to do anything with the returned value that the save method is returning we are capturing the argument / the object that is being passed to the save method at run time and we are asserting on that object

//Getting the value
Employee capturedEmployee = employeeArgumentCaptor.getValue();
assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());
//here we are verifying that what value that was passed over to the save method is actually equal to the value that is intended or not
//The value that is passed to the save method is actually the value that we wanted to pass there or not.Useful when we like do something like create inside the db,because while creating inside the db we want to test which thing was passed inside the save method because how else we will get hold of this that what are we actually storing/passing inside the save method at run time because this is not going to be returned
// We can test that the value that the value that is getting inside the db,going inside the db to be stored is actually the value that I want to pass in the db or not

    }
}


//When you RUN WITH COVERAGE you would be able to see that what all lines we were able to able to cover of the employee Service createNewMethod by running a particular test Case.The red lines are the one we were not able to cover



//Error : Error creating bean with name 'com.codingshuttle.Testing.App.services.impl.EmployeeServiceImplTest': Unsatisfied dependency expressed through field 'employeeService': No qualifying bean of type 'com.codingshuttle.Testing.App.services.EmployeeService' available: expected at least 1 bean which qualifies as autowire candidate.
//This error means that ->This is not able to create a bean of EmployeeServiceImplementation. WHY?
//This is happening because this @DataJpaTest annotation is not scaling the Service related code.This is only valid for the [Repository] and the [Entity] related code .This is not able to go through employeeService and not doing any dependency injection on EmployeeService.
//We can either add the @SpringBootTest annotation instead of @DataJpaTest.This will get the bean of EmployeeService , it will get an employee with id 1.We will get an error again but the error is employee with id 1 not found (ERROR : Employee not found with id:1) but we were able to call the getEmployeeById method
//With @SpringBootTest we were able to get the bean of employeeService,but we dont want to add this annotation as well because this is not integration testing . We dont want to start the entire SpringBootApplication and load the Security Context and do all that because the unit testing will get slower of the entire Spring Boot Application starts and the application Context (All the beans are registered that are required for the application to start).We are unit testing just the service layer so we need the bean for just the employee Service , we dont need to load any other beans and neither do not require to start the entire Spring boot Application just to test the service layer
//We are only interested in testing the service code , we should not be relying on the whole application context to provide the bean for employeeService.WHAT TO DO : In that case mocking comes into the picture
//MOCKING : We can say that JUnit start using mocking now,you already have a mocking extension start using that
//@ExtendWith(MockitoExtension.class)  : We are telling JUnit that you should start using mocking now because I need mocking in this particular class and we dont need to add any @Autowired now because that will not work , we just want to inject using the mock @InjectMocks
//@InjectMocks : used to inject a particular , inject all the mocks inside a particular bean and than create that bean.We currently dont have any mocks but we are injecting mocks .We are saying that create a bean and if you have any mocks in this class employeeService class that put all those mocks in this particular bean private EmployeeService employeeService; Currently we dont have any mocks inside the EmployeeService class
//We are again getting an error : Cannot instantiate @InjectMocks field named 'employeeService'! Cause: the type 'EmployeeService' is an interface. Because EmployeeService is an interface the actual implementation is provided by the employeeServiceImpl class so we have to InjectMocks for this class and not for the EmployeeService interface
//We are trying to create an instance(object,bean) of an employeeService , and currently since we are not using any dependency injection framework , we are using @InjectMocks -> This is not able to create a bean(or a class) of the EmployeeService(because it is an interface not a class and how can it make an instance or object of an interface).EmployeeService is an interface , how can it create an object of the interface.It is not using any dependency injection,so it does not know about the implementation of this EmployeeService as well.Because since , we are not starting the entire spring boot application and not using the @SpringBootTest annotation so no application context is being created , not Ioc contatiner no beans are being registered no component scanning is happening because we are not starting the spring boot application,so no dependency injection framework is being used(if no application context is being made and no beans are being registered than how dependency injection will work)
//So,to do that we will have to create an object of the concrete class which is EmployeeServiceImpl
//This time when we will run the application,we will see that the spring boot application is not started (before when we were using @SpringBootTest the whole spring boot application was starting the Spring image/Spring was written),but now when we will start it , the Spring will not be printed which means that Spring boot application is not starting and no application context has been loaded
//Now it should be able to create and inject the mocks of EmployeeServiceImpl,we were able to get the EmployeeById and able to call the method at least and we are getting the exception that we defined inside the EmployeeServiceImpl , that will be logged when the employee with the particular id will not be found in the db , so we were not able to create the employeeRepository object but we were able to call the getEmployeeById of the EmployeeServiceImpl,we were able to inject and mock EmployeeServiceImpl successfully.Now we have the bean for EmployeeServiceImpl but inside the getEmployeeById method of EmployeeServiceImpl we are using / calling the method of EmployeeRepository for that we need the object of employeeRepository which we do not have because no application context is being created so object will not be created for employeeRepository and hence the method of employeeRepository was not called which means that even if we have the employee with id 1 in the db we will get the error because the method findById of employeeRepository was not called only because we dont have the bean of employeeRepository
//Inside the actual EmployeeServiceImpl,we are not able to create an employeeRepository which is fine and thats why MOCKITO is there because we want to mock this behaviour [employeeRepository.findbyId(id)].We want to mock the whole employeeRepository inside the employeeService.We are going to use mock to do that

//----------------CREATING THE MOCK--------------------
//1.Create the mock using the @Mock annotation