package com.codingshuttle.Testing.App;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

//@SpringBootTest
//@Slf4j

class TestingAppApplicationTests {

//    private static final Logger log = LoggerFactory.getLogger(TestingAppApplicationTests.class);

    @BeforeEach //This method will be run before every test case,if we do not use any annotation before this method this would not run because it only runs the method annotated with @Test , such annotation given by junit
    void setUp() {
    //log.info("Starting the method, setting up config.This method runs before all the test cases");
    }
//used when ypu want to set up something before every test case.These are useful when you want to instantiate something when you want to create an instance that is being passed everywhere,you want to instantiate it and reset it before every test case than we use this annotation
//@AfterEach : Used after every method,if you want to remove something maybe you are creating some resource before every test case and you want to remove that resource after every test case than you will write this

    @AfterEach //Run after every test case
    void tearDown() {
        //log.info("Tearing down the method");
    }

//---------------OUTPUT FOR @AfterEach and @BeforeEach--------------
//03:26:22.061 [main] INFO com.codingshuttle.Testing.App.TestingAppApplicationTests -- Starting the method, setting up config.This method runs before all the test cases
//03:26:22.104 [main] INFO com.codingshuttle.Testing.App.TestingAppApplicationTests -- test one is run
//03:26:22.113 [main] INFO com.codingshuttle.Testing.App.TestingAppApplicationTests -- Tearing down the method
//03:26:22.164 [main] INFO com.codingshuttle.Testing.App.TestingAppApplicationTests -- Starting the method, setting up config.This method runs before all the test cases
//03:26:22.165 [main] INFO com.codingshuttle.Testing.App.TestingAppApplicationTests -- test two is run
//03:26:22.170 [main] INFO com.codingshuttle.Testing.App.TestingAppApplicationTests -- Tearing down the method

//We also have methods BeforeAll and AfterAll and these are run just once ,one time when the entire is being run not before and after all the test case
//We have ti mark the method as static because we are making it a BeforeALl because now the method is the property of the class
    @BeforeAll
   static void setUpOnce() {
        //log.info("Setup Once....");
    }

    @AfterAll
    static void tearDownOnce() {
        //log.info("Tearing down all....");
    }
    @Test
//    @Disabled
	void testNumberOne() {
//        log.info("test one is run");
	int a = 5;
    int b = 3;

    int result = addTwoNumbers(a,b);
// Check if this result is what we assert(expect) it to be or not.Here we have passed our own values of a and b,if we want to check that add function is doing its job or not we will say that the result coming from that function should be = 8 because we know that if this is the input given than 8 should be th expected output Any method that claims that it adds two numbers than it should return 8 for this particular input
//  Import assert method from junit and not from assertj,expected output is result and actual is 8
        //Assertions.assertEquals(result,8); //Check the methods , that whether it is solving the output correctly or not based on our inputs
// If expected and actual results are same than this test case will pass
//With assertions there is a problem of remembering all the methods that it contains.Ex: we can have different types of input->Long,Byte,Integer but all the methods that are related to different inputs are same(All the methods are : methodName[assertEquals] method).So we are not getting the support for different types of datatypes.For String we are not getting the support string has contains,startsWith all the methods and array can have size and all that we are not getting support for different types of datatypes because for all different tyoes method name is the name basically just like polymorphism and also we dont have support for chaining here.If we want to test two things one after the another we cant use chaining we have to use assertions for that and to avoid that we have a useful library called assertj
// AssertJ:Junit is a testing framework used to write the test cases and execute them
// AssertJ is not a framework for writing tests,this provides fluent and expressive assertions We need JUnit for a testing framework because we need a framework that can be used to run the test cases but we also want something better to write the test cases some fluent library to write the assertions,To have some good assertions we use assertJ library


// Import Assertions coming from assertj : We can put a long chain of assertions , since we are getting the result as 8 and also it is close to 9 by i offset also thats why test case will pass,if we write 7 in place of 8 than it will give an error that expected is 7 but method is returning 8,it will be the error message
//        Assertions.assertThat(result).isEqualTo(8)
//                .isCloseTo(9, Offset.offset(1));
// After this assertion we want to also check if this is closeTo some other expected value Ex: if the value method is returning is somewhere close to 9
//We can use it to test upon multiple things in a single chain
//assertThat this result and now we have different methods related to this result.Since the result is of type integer we are getting all the things that are supported by integer.Integer supports isCloseTo,isOne,isBetween,isEqualTo,isEven
// We have assertThat method and based upon different type of input this is going to spit out different types of methods that is supported by that input

//We used assertThat method for int,but we can also use it for String assertThat is a static method and we can import it statically
// OUTPUT : org.opentest4j.AssertionFailedError:
//expected: 7
// but was: 8
//Expected :7
//Actual   :8

//        assertThat(result).isEqualTo(7)
//                .isCloseTo(9, Offset.offset(1));

//        assertThat("apple")//Now this will give us all the methods supported by String Ex:equalTo.hasBoolean , we can convert it to Long,Byte,Short and check if this between these two strings or not if greater than another string or not,contains method and we will not get the integer related methods now
//                .isEqualTo("Apple")
//                .startsWith("App")
//                .endsWith("le")
//                .hasSize(5);

// It is recommended to add more and more assertThat method but also remember to follow single responsibility principle of solid principle A method that is supposed to do one thing should only do that thing it should not do any other thing
// if we are writing the test case for handling addition function than it should only do/handle addition method only for one particular test case only
// As soon as the first test case will fail all the other test cases will not run in that chain
    }

    @Test
//    @DisplayName("displayTestNameTwo")
    void testNumberTwo() {
////        log.info("test two is run");
//
//    int  a = 5;
//    int b = 3;
//    b = 0;
//
//    double result = divideTwoNumbers(a,b);
//    System.out.println(result);
////Since we have made it a double return type as double so 5/0 is giving us infinity and no exception is being thrown from the method even after dividing it by 0
////OUTPUT : Infinity when a = 5 and b = 0 , remove double.Now we will get the exception
//// After removing double : java.lang.ArithmeticException: / by zero.We want to check that if we are getting this exception correctly or not
//
    }

@Test
void testDivideTwoNumbers_whenDenominatorIsZero_ThenArithmeticException() {
        int a = 5;
        int b = 0;
//Correct Exception are thrown for different types of use cases or not

        assertThatThrownBy(() -> divideTwoNumbers(a,b))
                //.isInstanceOf(ArithmeticException.class);//If the exception we are getting is an instance of Arithmetic Exception or not,we are going to assert on that
             //   .isInstanceOf(RuntimeException.class) //It is a type of ArithmeticException so the test case will run and Exception.class will also work
               // .isInstanceOf(NullPointerException.class); //Test case will fail now because we are asserting that it should give NullPointerException when divided by 0 , so the test case is wrong
                .isInstanceOf(ArithmeticException.class)
                .hasMessage("Tried to divide by Zero"); //Assert that we are getting ArithmeticException and also assert that the message of the exception is this only


//We have to pass in the lambda that actually takes in that method and call method divideTwoNumbers and we can assert on the exception that we rae getting from there.We can assert that if we are getting an arithmetic Exception or nor
//The exception we are getting is an instance of ArithmeticException or not we want to assert on that,even if code is giving an error the test case is working fine because test case is actually doing what it is supposed to do
// The test case is actually supposed to check that whether the method is returning an ArithmeticException or not , if we divide by 0
    }

int addTwoNumbers(int a,int b){
        return a+b;
}

double divideTwoNumbers(int a,int b){
        try{
//            return (double) a/b;
        return  a/b;
        }
        catch(ArithmeticException e){
           // log.info("Arithmetic Exception occurred : "+e.getLocalizedMessage());
//            throw new ArithmeticException(e.getLocalizedMessage());
            throw new ArithmeticException("Tried to divide by Zero");

        }
}
}



//Inside target we have jacoco.exec which contains the details of whole report inside the binary format.This binary format can be useful for some othe rthird party softwares that can read jacoco.exec and then generate diagrams and dashboards for you
//inside target>site>open main (index.html) and open it in any of the browsers
//Everytime you do package , the test cases are run and the report gets generated




// WebTestClient acts as a "wrapper" around the standard WebClient, adding a powerful fluent API for assertions. It allows you to:Send Requests: Prepare and execute HTTP requests (GET, POST, etc.) with a readable, chainable syntax.Verify Responses: Check the status code, headers, and body of a response in a single expression.Test Without a Server: It can bind directly to your controllers or application context to run tests using mock request and response objects, which is much faster than starting a real server.Test Real Servers: It can also perform full end-to-end integration tests by connecting to a live running server (usually on a RANDOM_PORT).
