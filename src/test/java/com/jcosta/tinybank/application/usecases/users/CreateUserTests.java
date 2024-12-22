package com.jcosta.tinybank.application.usecases.users;

import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import io.quarkus.test.Mock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreateUserTests {


    private CreateUser createUser;
    private UserDataService userDataService;

    @BeforeEach
    public void setup(){
        this.userDataService = mock(UserDataService.class);
        this.createUser = new CreateUser(userDataService);
    }

    @Test
    public void when_user_already_exists_should_give_conflict() {
        // Arrange
        doReturn(new BusinessException(ExceptionCode.DUPLICATE_EXCEPTION, "Duplicate Record" )).when(this.userDataService.create(any()));

        // Act
        // Assert
    }

    @Test
    public void when_valid_input_should_return_the_created_user(){
        // Arrange
        // Act
        // Assert

    }

    @Test void when_invalid_input_should_return_validation_exception() {
        // Arrange
        // Act
        // Assert
    }
}
