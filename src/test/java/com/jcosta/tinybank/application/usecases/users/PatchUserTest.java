package com.jcosta.tinybank.application.usecases.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import com.github.fge.jsonpatch.JsonPatch;
import com.jcosta.tinybank.application.port.AccountsDataService;
import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.DomainException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.users.User;
import com.jcosta.tinybank.domain.users.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PatchUserTest {

    private UserDataService userDataService;
    private AccountsDataService accountsDataService;
    private PatchUser patchUser;

    private User mockUser;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        this.mapper = new ObjectMapper();
        userDataService = mock(UserDataService.class);
        accountsDataService = mock(AccountsDataService.class);
        patchUser = new PatchUser(userDataService, accountsDataService, this.mapper);
        mockUser = new User(UUID.randomUUID().toString(), "John Doe", UserStatus.ACTIVE);
    }

    @Test
    public void when_patchIsValid_then_userShouldBePatchedSuccessfully() throws Exception {
        // Arrange
        when(userDataService.get(mockUser.id(), true)).thenReturn(mockUser);
        when(userDataService.update(mockUser)).thenReturn(true);
        String patch = "[{\"op\": \"replace\", \"path\": \"/status\",\"value\": \"ACTIVE\"}]";
        JsonPatch mockPatch = mapper.readValue(patch, JsonPatch.class);

        // Act
        boolean result = patchUser.execute(mockUser.id(), mockPatch);

        // Assert
        assertTrue(result);
        verify(userDataService, times(1)).get(mockUser.id(), true);
        verify(userDataService, times(1)).update(mockUser);
    }

    @Test
    public void when_patchContainsInvalidOperation_then_throwDomainException() throws JsonPointerException, JsonProcessingException {
        // Arrange
        when(userDataService.get(mockUser.id(), true)).thenReturn(mockUser);
        String patch = "[{\"op\": \"add\", \"path\": \"/status\",\"value\": \"ACTIVE\"}]";
        JsonPatch mockPatch = mapper.readValue(patch, JsonPatch.class);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> {
            patchUser.validate(mockPatch);
        });
        assertEquals("Invalid operation, valid operations: replace", exception.getInvalidParams().get(0).getReason());
    }

    @Test
    public void when_patchContainsInvalidPath_then_throwDomainException() throws JsonProcessingException {
        // Arrange
        when(userDataService.get(mockUser.id(), true)).thenReturn(mockUser);
        String patch = "[{\"op\": \"replace\", \"path\": \"/username\",\"value\": \"ACTIVE\"}]";
        JsonPatch mockPatch = mapper.readValue(patch, JsonPatch.class);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> {
            patchUser.validate(mockPatch);
        });

        assertEquals("Invalid path, valid paths: /status", exception.getInvalidParams().get(0).getReason());
    }

    @Test
    public void when_patchContainsInvalidValue_then_throwDomainException() throws JsonProcessingException {
        // Arrange
        when(userDataService.get(mockUser.id(), true)).thenReturn(mockUser);
        String patch = "[{\"op\": \"replace\", \"path\": \"/status\",\"value\": \"invalid\"}]";
        JsonPatch mockPatch = mapper.readValue(patch, JsonPatch.class);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> {
            patchUser.validate(mockPatch);
        });

        assertEquals("Invalid value option, valid value options: ACTIVE,INACTIVE", exception.getInvalidParams().get(0).getReason());
    }

    @Test
    public void when_userNotFound_then_throwBusinessException() throws JsonProcessingException {
        // Arrange
        when(userDataService.get(mockUser.id(), true)).thenReturn(null);
        String patch = "[{\"op\": \"replace\", \"path\": \"/status\",\"value\": \"ACTIVE\"}]";
        JsonPatch mockPatch = mapper.readValue(patch, JsonPatch.class);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            patchUser.execute(mockUser.id(), mockPatch);
        });

        assertEquals(ExceptionCode.NOT_FOUND_EXCEPTION, exception.getCode());
        assertEquals("user with id ["+mockUser.id()+"] not fount", exception.getMessage());
    }
}
