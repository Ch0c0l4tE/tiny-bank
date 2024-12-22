package com.jcosta.tinybank.application.usecases.users;

import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.domain.Search;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.users.User;
import com.jcosta.tinybank.domain.users.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CreateUserTest {

    private UserDataService userDataService;
    private CreateUser createUser;
    private CreateUserRequest validRequest;
    private CreateUserRequest duplicateRequest;

    @BeforeEach
    public void setUp() {
        userDataService = mock(UserDataService.class);
        createUser = new CreateUser(userDataService);
        validRequest = new CreateUserRequest("john.doe");
        duplicateRequest = new CreateUserRequest("taken.username");
    }

    @Test
    public void when_usernameIsUnique_then_userIsCreated() {
        // Arrange
        when(userDataService.search(validRequest.username(), null, null, true))
                .thenReturn(new Search<>(new ArrayList<>(), null, 10));

        // Arrange
        when(userDataService.create(any(User.class)))
                .thenReturn(new User(null, validRequest.username(), UserStatus.ACTIVE));

        // Act
        User createdUser = createUser.execute(validRequest);

        // Assert
        assertNotNull(createdUser);
        assertEquals(validRequest.username(), createdUser.username());
        assertEquals(UserStatus.ACTIVE, createdUser.status());
        verify(userDataService, times(1)).search(validRequest.username(), null, null, true);
        verify(userDataService, times(1)).create(any(User.class));
    }

    @Test
    public void when_usernameIsTaken_then_throwBusinessException() {
        // Arrange
        when(userDataService.search(duplicateRequest.username(), null, null, true))
                .thenReturn(new Search<>(List.of(new User(UUID.randomUUID().toString(), "test", UserStatus.ACTIVE)), null, 10));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            createUser.execute(duplicateRequest);
        });

        assertEquals(ExceptionCode.DUPLICATE_EXCEPTION, exception.getCode());
        assertEquals("username already being used", exception.getMessage());
        verify(userDataService, times(1)).search(duplicateRequest.username(), null, null, true);
        verify(userDataService, never()).create(any(User.class));
    }
}