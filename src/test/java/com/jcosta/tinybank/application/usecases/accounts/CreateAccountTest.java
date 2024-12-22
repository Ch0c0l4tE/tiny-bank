package com.jcosta.tinybank.application.usecases.accounts;

import com.jcosta.tinybank.application.port.AccountsDataService;
import com.jcosta.tinybank.application.port.UserDataService;
import com.jcosta.tinybank.domain.accounts.Account;
import com.jcosta.tinybank.domain.accounts.AccountStatus;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateAccountTest {

    private AccountsDataService accountsDataService;
    private UserDataService userDataService;

    private CreateAccount createAccount;

    private CreateAccountRequest validRequest;
    private CreateAccountRequest invalidRequest;

    private User mockUser;
    private UUID mockedUserId;

    @BeforeEach
    public void setUp() {
        this.userDataService = mock(UserDataService.class);
        this.accountsDataService = mock(AccountsDataService.class);
        this.createAccount = new CreateAccount(this.accountsDataService, this.userDataService);
        this.mockedUserId = UUID.randomUUID();
        validRequest = new CreateAccountRequest(this.mockedUserId.toString(), 1000L);
        invalidRequest = new CreateAccountRequest(this.mockedUserId.toString(), 1000L);
        mockUser = new User(this.mockedUserId.toString(), "John Doe", UserStatus.ACTIVE);
    }

    @Test
    public void when_valid_owner_should_return_success_creation() {
        // Arrange
        when(userDataService.get(validRequest.ownerId(), false)).thenReturn(mockUser);
        when(accountsDataService.create(any(Account.class))).thenReturn(new Account(
                null, validRequest.ownerId(), validRequest.balance(), AccountStatus.ACTIVE
        ));

        // Act
        Account createdAccount = createAccount.execute(validRequest);

        // Assert
        assertNotNull(createdAccount);
        assertEquals(validRequest.ownerId(), createdAccount.ownerId());
        assertEquals(validRequest.balance(), createdAccount.balance());
        assertEquals(AccountStatus.ACTIVE, createdAccount.status());

        verify(userDataService, times(1)).get(validRequest.ownerId(), false);
        verify(accountsDataService, times(1)).create(any(Account.class));
    }

    @Test
    public void when_owner_not_found_should_return_business_exception() {
        // Arrange
        when(userDataService.get(invalidRequest.ownerId(), false)).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            createAccount.execute(invalidRequest);
        });

        // Assert
        assertEquals(ExceptionCode.NOT_FOUND_EXCEPTION, exception.getCode());
        assertEquals("user with id ["+this.mockedUserId.toString()+"] not fount", exception.getMessage());
        verify(userDataService, times(1)).get(invalidRequest.ownerId(), false);
        verify(accountsDataService, never()).create(any(Account.class));
    }
}
