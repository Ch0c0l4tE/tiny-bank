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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetAccountByIdTest {

    private AccountsDataService accountsDataService;

    private UserDataService userDataService;

    private GetAccountById getAccountById;

    private Account mockAccount;
    private User mockUser;
    private UUID mockedUserId;

    @BeforeEach
    public void setUp() {
        this.userDataService = mock(UserDataService.class);
        this.accountsDataService = mock(AccountsDataService.class);
        this.getAccountById = new GetAccountById(this.accountsDataService, this.userDataService);
        this.mockedUserId = UUID.randomUUID();
        mockAccount = new Account("1", mockedUserId.toString(), 1000L, AccountStatus.ACTIVE);
        mockUser = new User(mockedUserId.toString(), "John Doe", UserStatus.ACTIVE); 
    }

    @Test
    public void when_accountExistsAndOwnerExists_then_returnAccount() {
        // Arrange
        when(accountsDataService.get(mockAccount.id(), true)).thenReturn(mockAccount);
        when(userDataService.get(mockAccount.ownerId(), false)).thenReturn(mockUser);

        // Act
        Account result = getAccountById.execute(mockAccount.id());

        // Assert
        assertNotNull(result);
        assertEquals(mockAccount.id(), result.id());
        assertEquals(mockAccount.ownerId(), result.ownerId());
        verify(accountsDataService, times(1)).get(mockAccount.id(), true);
        verify(userDataService, times(1)).get(mockAccount.ownerId(), false);
    }

    @Test
    public void when_accountDoesNotExist_then_throwBusinessException() {
        // Arrange
        when(accountsDataService.get(mockAccount.id(), true)).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            getAccountById.execute(mockAccount.id());
        });

        assertEquals(ExceptionCode.NOT_FOUND_EXCEPTION, exception.getCode());
        assertEquals("account with id [1] not fount", exception.getMessage());
        verify(accountsDataService, times(1)).get(mockAccount.id(), true);
        verify(userDataService, never()).get(anyString(), anyBoolean());
    }

    @Test
    public void when_ownerDoesNotExist_then_throwBusinessException() {
        // Arrange
        when(accountsDataService.get(mockAccount.id(), true)).thenReturn(mockAccount);
        when(userDataService.get(mockAccount.ownerId(), false)).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            getAccountById.execute(mockAccount.id());
        });

        assertEquals(ExceptionCode.NOT_FOUND_EXCEPTION, exception.getCode());
        assertEquals("owner with id [1] not fount", exception.getMessage());
        verify(accountsDataService, times(1)).get(mockAccount.id(), true);
        verify(userDataService, times(1)).get(mockAccount.ownerId(), false);
    }
}