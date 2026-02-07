package com.codingshuttle.Testing.App;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestContainerConfiguration {

//Create a bean
//Define a bean
//This is an instance of PostgreSQL container and this is returning a PostgreSQLContainer which can contain a docker image and this is the docker image we are using currently
//You can use docker image related to a particular version as well but it is recommended to use the latest
//basically this is , this(postgres:latest) should be equal to the production db
//You want your testing environment to be as close as your production environment so this db (postgres:latest) should be corresponding to the production  db that you have
// If we are using the latest db in the production db than you can use latest here
//If you want to use something else may be 14.0  we can use 14.0 as well postgres:+14.0
//This will create(postgres:latest) an image of , create the docker image of this particular tag postgres:latest inside the docker container and than it will run that docker container just for the process of running my test cases
// And after that it will also remove that test container from there,we have to import the TestContainer inside the repository code as well

//    @Bean
//    @ServiceConnection
//    PostgreSQLContainer<?> postgresContainer() {
//        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
//    }


    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>("postgres:latest")
                .withEnv("TZ", "Asia/Kolkata")
                .withEnv("PGTZ", "Asia/Kolkata")
                .withReuse(true);
    }

}



//ERROR : .BeanCreationException: Error creating bean with name 'testingAppApplication': Error creating bean with name 'postgresContainer' defined in class path resource [com/codingshuttle/Testing/App/TestContainerConfiguration.class]: Could not find a valid Docker environment. Please see logs and check configuration
//This error is coming because of the version mismatch
//Earlier the project was using spring-boot-starter-parent version 3.3.2 . After updating it to version 3.5.8,the test cases will start running successfully.
