package com.codingshuttle.Testing.App.repositories;

import com.codingshuttle.Testing.App.TestContainerConfiguration;
import com.codingshuttle.Testing.App.entities.Employee;
import lombok.RequiredArgsConstructor;
import org.apiguardian.api.API;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;


@Import(TestContainerConfiguration.class) //After importing,this will actually run the bean automatically and when you run the test cases it will run the docker container for us,it will run the docker image.Currently, h2 in memory db is also been initialized / initiated because this is the default property of the @DataJpaTest.It is connected to the docker : : Reusing existing container (7d813a6bd344bc8d318ea48a9a0de3b017f1ca4048828f3104572667fb446cb7) and not creating a new one
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //This means that you dont need , you dont want to replace your real db.Basically,what it is doing is it is not replacing the real db and since we have already imported the test container so it is using that container on your behalf . Starting the docker image pulling the test container , creating the postgre test image for you and than connecting the spring boot application with the container and also dumping that connection once the test is done(shutting down the connection)
//@RequiredArgsConstructor
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository; //Injection done , handled by @DataJpaTest


//Create an employee
    private Employee employee;

//    private EmployeeDto employeeDto;
//But for this particular test which is EmployeeRepositoryTest , we dont need a dto


    @BeforeAll
    static void setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
    }



    //This method will run before every test case and this method will set up /create an employee before every testcase
    @BeforeEach
    void setUp() {
// We have build the employee now we can use this employee inside each of the test cases
        employee = Employee.builder()
//                .id(1L)
                .name("Anuj")
                .email("anuj@gmail.com")
                .salary(100L)
                .build();
    }


    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee()
    {
//  ARRANGE
//Step 1 : Arrange the test case,to do that we are going to input something in our db .To do that we will use
        employeeRepository.save(employee);
// This will simply save an employee inside the test db(As we are running the test db and we have used @DatajpaTest while running test cases it will use test db only to perform operations)

//  ACT : Acting on the method
// Step 2 : Act upon this,After adding the employee inside the database now we want to act upon this
// For acting , we call the findByEmail method.To test the method we have to act upon this method only.This method findByEmail is supposed to return the List<Employee>
        List<Employee> employeeList = employeeRepository.findByEmail(employee.getEmail()); //employee.getEmail()->This email is coming from the employee added in the db,Since we have already added the employee inside the test db,we are supposed to get the employee back from this,finding by email which is already present inside the in memory db
// In here we have to pass the email as anuj@gmail.com because this is the user email present inside the in memory db.Because in this test case,we are testing on the email which is valid

//Step 3 : After acting,Now we have the employee List , we have to assert something on this employee.We have something in the employeeList and we are supposed to get something because we have added the same employee inside the test db just before fetching the employee from the test db with the same email.findByEmail is supposed to give the employeeList that is not empty so we can assert on that.We can assert that the employeeList is not empty
// assertThat the employeeList is not null

    assertThat(employeeList).isNotNull();

      assertThat(employeeList).isNotEmpty();

//And we can also assert on the type of data that we are getting from the employeeList.The employee we are getting is actually the same employee or not that we inserted.It is possible that findByEmail is working but giving some random employee not giving the employee related to the email we are passing
//        assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail()); //Is is supposed to give only one employee with that email
//We are asserting that the employeeList that we are getting has one data and the email that we are getting from there is equal to the email which was passed/stored in the db
// Run the test case and the test will run and we will see some hibernate related things
//First hibernate is creating a db table and we are inserting into the employee,which we are doing when we are saving the employee that we have created inside the in memory db  and than selecting on the basis of email id and once it has done all the test case are run successfully.Thi stest case is doing what it is expected to do
    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmptyEmployeeList() {
//Given
// In this test case,we dont need to save an employee because we want tot get an employee List which is not present
String email = "notPresent.123@gmail.com";


//When : Passing email which is not present in the db
List<Employee> employeeList = employeeRepository.findByEmail(email);

// Then : When email is not present than return empty employeeList
        assertThat(employeeList).isNotNull();
       assertThat(employeeList).isEmpty();//It is supposed to be empty,it should not contain anything
     //assertThat(employeeList).isNotEmpty();//The test case will fail Error : java.lang.AssertionError:
//        Expecting actual not to be empty

    }
}

//We have three things while writing the test :
//1.Arrange(Given) : We arrange the test case like how do you want to test this?
//Here,We want to pass an employee and when we call the findByEmail method present inside the repository,than we should get that employee.In order to do that we first have to add an employee inside the inmemory db so that we can get that employee currently we dont have any employee that is stored in the database so first we have to arrange the database so that employee can be fetched from that db , because we are not using production db here.First add the employee to get the employee
//Currently,when the test case is run we dont have an employee that is stored inside the db,first arrange the db like -> We first have to create an employee
//2.Act(When) :
//3.Assert(Than) :





//We write test cases for each of the scenerio,since this test is carried is out on the repository level and since this layer does not contain much of the business logic so we do not have much of the test cases to write here,it makes no sense to write more test cases here in the repository.But in service layer we have business logic so we will write test cases there
//We can also do run with coverage and this will generate a coverage report for this particular test file and you can see what different types of line it is hitting
//So employee repository is hitting all the 100% of the test cases.All the 100% of the methods and lines are hit and thats what we want we want coverage to be as high as possible
//Currently, we are using the in memory db .Now there is nothing wrong in using an in memory db as long as your project is very low level project and you are not writing very - high production ready code.You are not working on a production environment and your db is also very simple your use case is simpl ethan there is nothing wrong with using an in memory testing db
//But sometimes,in production they do not use an in memory testing db for testing purposes what they want is to have a db that resembles as close as their real (production) db and thats how they gain confidence
//Because somethings might be working in the real production db but they are not supported in the h2 in memory db because h2 in memory db is a very simple kind of db.It does not have all the functionalities
//We want in that case we want to have a testing db that ressembles as close as your production db
//To do that you can have two things :
//1.You can either spin up a new testing db in production
//We can have another testing db that is hosted somewhere and than you can use that testing db and configure that db as well with the help of profiles and that testing db could be used just for testing purposes but it is an overkill because for that you have to maintain a separate testing db(db) which is meant just for testing purposes and you are not testing everyday you are not testing all the time.You are not using that testing db at all time so it is an overkill to have another testing db.Some companies have testing db as well separately
//2.But we can use a custom docker image of a db and you can use that db as copy of real db
//We can do that with the help of TestContainer
//Once docker is downloaded you can simply run that docker and leave that docker , you dont have to configure anything in docker you can simply leave it running and after that we have to configure the application to use test containers
//Inside docker , go t guides and search for spring boot (Getting started with TestContainers in spring boot project) and this list all the things that are needed in order for test containers to work
//Or else to start.spring.io and from there choose Maven->Java->and than we can add the dependency for test container from there. TestContainer and also add the PostgreSQLDriver dependency->do explore(Nt GENERATE) and than you will find all the dependencies tat are needed for the tets container to work,we already have the starter dependency and the postgreSQL dependency so we dont need these two dependency .Copy thr remaining dependency and paste it in pom.xml
//You dont have to anything after running docker .We can have a lot of images in docker Open docker desktop -> GO to images .Make sure that docker engine is currently running and it will run automatically when docker desktop is open
//Just keep the docker instance running after that we will have to go inside application.properties and define some configuration






//For understanding aboout docker use this :
//https://chatgpt.com/share/69870077-42a8-8012-87f3-12cfc656f281
//After changing the version also the time zone error was coming and the timeZone was solved by changing the JVM timeZone because the older versions use older time Zone
//Changing the timeZone to -ea -Duser.timezone=Asia/Kolkata ea is for enable assertions
//After that the second test case was failing due to the id issue because while creating the employee we rae passing th eid also but database autogenerated the id and it overides the id that we rae passing with the id created by the db So while we are trying to get the employee with the id 1L it will give error that no employee exists with such id,because user given id gets replaced by the db generated id So,while creating the employee dont pass the id teh id will be automatically generated




//It is creating the docker image
//2026-02-07T14:37:36.659+05:30  INFO 12544 --- [Testing-App] [           main] org.testcontainers.DockerClientFactory   : Connected to docker:
//Server Version: 29.2.0
//API Version: 1.53
//Operating System: Docker Desktop
//Total Memory: 1864 MB
//It is creating the docker image postgres:latest (postgres : created with the latest tag) you can see the image inside the docker desktop and a tetsContainer will also be created .In that container we have this image postgre that was created for us .Springboot made the db connection for use using @ServiceConnection and it ran the test case
//When the test cases wer finished executing it stopped the container also,since we have .testcontainer.properties we have set that dont delete the container it will not delete it
//But if we remove that,once the test cases are runned the docker instance will stop running and delete the container
//If you want to test cases on the real db because you want the testing environment as closely as possible to production environment
//If you are doing something on production db and seeing it on h2 in memory db you cannot say with 100% confidence that this thing will run oin production db also because of change in environment you can have problem like Run on your computer
//Runs on a machine so to avoid that we use testContainers
//These test containers can spin up a real db for you and they can only run for the duration of your test cases
//Currently both of the db are running , in memory db is also running (h2 db) and the postgre SQL db is also running to avoid that we can do two things:
//We can remove the h2 db configuration from the pom.xml (remove its dependency) or we can do replace.NONE . Now this will not replace and also it will not run the real db
//This will only run the docker container db . Now the h2 in memory db is not running and for getting the db it is actually going to docker only and than it is using the docker connection to make the postgre SQL connection
//We can go to pom.xml and remove the h2 dependency because now we do not need that dependency.This time the h2 in memory db will not be created for you



//Whenever you get an entityManager error/not able to load Application Context ,just edit configurations and set the timeZone -> -Duser.timezone=Asia/Kolkata