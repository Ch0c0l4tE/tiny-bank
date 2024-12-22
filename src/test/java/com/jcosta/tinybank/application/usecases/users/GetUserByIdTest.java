package com.jcosta.tinybank.application.usecases.users;

import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.users.User;
import com.jcosta.tinybank.domain.users.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetUserByIdTest {

    private UserDataService userDataService;
    private GetUserById getUserById;

    private User mockUser;
    private UUID mockUserId;

    @BeforeEach
    public void setUp() {
        userDataService = mock(UserDataService.class);
        getUserById = new GetUserById(userDataService);
        mockUserId = UUID.randomUUID();
        mockUser = new User(mockUserId.toString(), "John Doe", UserStatus.ACTIVE);
    }

    @Test
    public void when_userExists_then_returnUser() {
        // Arrange
        when(userDataService.get(mockUser.id(), true)).thenReturn(mockUser);

        // Act
        User result = getUserById.execute(mockUser.id());

        // Assert
        assertNotNull(result);
        assertEquals(mockUser.id(), result.id());
        assertEquals(mockUser.username(), result.username());
        verify(userDataService, times(1)).get(mockUser.id(), true);
    }

    @Test
    public void when_userDoesNotExist_then_throwBusinessException() {
        // Arrange
        when(userDataService.get(mockUser.id(), true)).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            getUserById.execute(mockUser.id());
        });
        assertEquals(ExceptionCode.NOT_FOUND_EXCEPTION, exception.getCode());
        assertEquals("user with id ["+mockUserId.toString()+"] not fount", exception.getMessage());
        verify(userDataService, times(1)).get(mockUser.id(), true);
    }
}