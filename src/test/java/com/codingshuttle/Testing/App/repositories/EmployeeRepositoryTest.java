package com.codingshuttle.Testing.App.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) //This will replace real database(postgre db) with the autoconfigured db .When we run the test case we will see that it will use the embedded db.This db (embedded db h2 db) was added by us in pom.xml thats why our we have added the two db postgresql db which will be used for development purposes only  as well as the h2 db can be used for testing purposes so that when you are running your test cases you dont have to pollute the real db and this is actually a recommended practice
//One more recommended practice is to use another db all together , a real db that mimics postgre sql db because h2 db however efficient may be it is not actually depicting the production db , postgresql-> this is the production db and currently we are using h2 db for testing purposes,so we will replace h2 db , for now all our test cases would be run on h2 db only
    

class EmployeeRepositoryTest {
//We have to test for the findByEmail method present inside the repository
//Nomenclature is imp,how do you define the method name is really imp because by that everyone in the company will understand what kind of scenerio you want to test
//We want to test  a scenerio where if we pass a valid  email than we will get a List of Employees that contains the employee with a valid email

//No need to use the constructor injection here because these tests are not run very frequently as supposed to your normal class methods because those are run for each API call (for each http requests that the client is sending) but these test cases are not run frequently we can directly import like this
//Since this is Autowired,you have to make this class a Bean as well,you have two options : 1.Using @SpringBootTest : you can use this annotation as well,earlier when we were not using this annotation the test cases could run in lets say 10ms(very fast) and you want your unit test to be very fast but now we have added this annotation
// Due to this @SpringBootTest annotation your test case will now take very long to execute because this annotation will spin up the whole spring boot application and than it will load the spring boot web as well,data jpa related(hibernate) dependency as well and the autoconfiguration.It will do the whole thing to run your test .Now the test is taking Ex:301 ms to run

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void testFindByEmail_whenEmailIsValid_thenReturnEmployee() {
//We are writing a test for this particular method which is findByEmail and you are testing this particular scenerio wherein you pass a valid email and thi should return a valid employee and test method should not return anything the return type should be void
//We can create one more tests for this method , as many no of tests as we want for this particular method

//For implementing these two methods we have to have EmployeeRepository because we are testing EmployeeRepository so we have to inject it here
//The first method that is run in a file,will actually catch the whole application context and the next method in the file can use that catched application context .The first test case took 300ms but the second test method only took 1ms because the first method was used to spin up the application context and once that was done the second method actually used the catched application context
//Now we can use the employee Repository to test upon these things
// @SpringBootTest : By using this dependency , we are using the whole spring boot application this is also starting the real dev database.This is using the postgres SQL connection which is the real db that we have set up ,this is not using the in memory db that we have set up for testing purposes which is h2 db it is not using that we have to configure it to use that
// To configure h2 db we have another @AutoConfigureTestDatabase we can use this if we want to configure the test database

employeeRepository.findByEmail("");
    }

//Test for te scenerio where we pass in the invalid email and than in that case it should not return the employee
    @Test
    void testFindByEmail_whenEmilIsNotFound_thenReturnEmptyEmployeeList() {

    }
}

//We will see that ,it creates a new file EmployeeRepositoryTest inside the repositories package.We have java inside main src->main->java->all packages listed inside main
//Similar structure is followed inside the tests as well,resides at similar level as the main.Inside main you write main development code and inside tests we write tests
//Similar structure for main and tests EmployeeRepository is the development file and in tests EmployeeRepositoryTest is the testing file.This shows that test is written for the repository development code


//When we remove @SpringBootTest annotation we will get this error java.lang.NullPointerException: Cannot invoke "com.codingshuttle.Testing.App.repositories.EmployeeRepository.findByEmail(String)" because "this.employeeRepository" is null
//https://chatgpt.com/share/69836652-7fac-8012-ac12-5fe7747cc832