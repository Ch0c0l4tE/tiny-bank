package com.jcosta.tinybank.application.usecases.users;

import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.users.User;
import com.jcosta.tinybank.domain.users.UserStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        String username = "test";
        doReturn(new Search<>(List.of(new User(UUID.randomUUID().toString(), username, UserStatus.ACTIVE)), "0"))
                .when(this.userDataService).search(username, null, null, false);

        // Act & Assert
        Assertions.assertThrows(
                BusinessException.class,
                () -> this.createUser.execute(new CreateUserRequest(username)));
        verify(this.userDataService).search(username, null, null, false);
    }

    @Test
    public void when_valid_input_should_return_the_created_user(){
        // Arrange
        String username = "test";
        doReturn(new Search<>(List.of(), null))
                .when(this.userDataService).search(username, null, null, false);

        User mockedUser = new User(null, username, UserStatus.ACTIVE);

        doReturn(mockedUser)
                .when(this.userDataService).create(mockedUser);
        // Act
        User user = this.createUser.execute(new CreateUserRequest(username));

        // Assert
        Assertions.assertEquals(username, user.username());
        verify(this.userDataService).search(username, null, null, false);
        verify(this.userDataService).create(user);
    }
}
