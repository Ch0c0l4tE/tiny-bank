package com.jcosta.tinybank.application.usecases.transactions;

import com.jcosta.tinybank.application.port.AccountsDataService;
import com.jcosta.tinybank.application.port.TransactionsDataService;
import com.jcosta.tinybank.domain.accounts.Account;
import com.jcosta.tinybank.domain.accounts.AccountStatus;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.DomainException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.transactions.Transaction;
import com.jcosta.tinybank.domain.transactions.TransactionOperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateTransactionTest {

    private TransactionsDataService transactionsDataService;
    private AccountsDataService accountsDataService;
    private CreateTransaction createTransaction;

    private Account sourceAccount;
    private Account targetAccount;

    @BeforeEach
    public void setUp() {
        transactionsDataService = mock(TransactionsDataService.class);
        accountsDataService = mock(AccountsDataService.class);
        createTransaction = new CreateTransaction(transactionsDataService, accountsDataService);
        sourceAccount = new Account("source-id", "owner-id", 100L, AccountStatus.ACTIVE);
        targetAccount = new Account("target-id", "owner-id", 50L, AccountStatus.ACTIVE);
    }

    @Test
    public void when_operationTypeIsValid_then_transactionShouldBeCreatedSuccessfully() {
        // Arrange
        CreateTransactionRequest request = new CreateTransactionRequest(
                TransactionOperationType.DEPOSIT.toString(), "source-id", "target-id", 50L);

        // Arrange
        when(accountsDataService.get("target-id", false)).thenReturn(targetAccount);
        Transaction mockTransaction = new Transaction(
                TransactionOperationType.DEPOSIT, "source-id", "target-id", 50L, Instant.now().toEpochMilli());
        when(transactionsDataService.create(any(Transaction.class))).thenReturn(mockTransaction);

        // Act
        Transaction result = createTransaction.execute(request);

        // Assert
        assertNotNull(result);
        assertEquals(TransactionOperationType.DEPOSIT, result.operationType());
        assertEquals(50.0, result.amount());
        verify(accountsDataService, times(1)).get("target-id", false);
        verify(accountsDataService, times(1)).update(any(Account.class));
        verify(transactionsDataService, times(1)).create(any(Transaction.class));
    }

    @Test
    public void when_operationTypeIsInvalid_then_throwDomainException() {
        // Arrange
        CreateTransactionRequest request = new CreateTransactionRequest(
                "INVALID_OPERATION", "source-id", "target-id", 50L);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> {
            createTransaction.execute(request);
        });

        assertEquals("Invalid operationType, must be one of: TRANSFER,WITHDRAW,DEPOSIT", exception.getInvalidParams().get(0).getReason());
    }

    @Test
    public void when_sourceAccountDoesNotExist_then_throwBusinessException() {
        // Arrange
        CreateTransactionRequest request = new CreateTransactionRequest(
                TransactionOperationType.TRANSFER.toString(), "source-id", "target-id", 50L );

        // Arrange
        when(accountsDataService.get("source-id", false)).thenReturn(null);
        when(accountsDataService.get("target-id", false)).thenReturn(targetAccount);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            createTransaction.execute(request);
        });

        assertEquals(ExceptionCode.NOT_FOUND_EXCEPTION, exception.getCode());
        assertEquals("source account with id [source-id] not fount", exception.getMessage());
    }

    @Test
    public void when_targetAccountDoesNotExist_then_throwBusinessException() {
        // Arrange
        CreateTransactionRequest request = new CreateTransactionRequest(
                TransactionOperationType.TRANSFER.toString(), "source-id", "target-id", 50L);
        when(accountsDataService.get("source-id", false)).thenReturn(sourceAccount);
        when(accountsDataService.get("target-id", false)).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            createTransaction.execute(request);
        });

        assertEquals(ExceptionCode.NOT_FOUND_EXCEPTION, exception.getCode());
        assertEquals("target account with id [target-id] not fount", exception.getMessage());
    }

    @Test
    public void when_sourceAccountHasInsufficientFunds_then_throwBusinessException() {
        // Arrange
        CreateTransactionRequest request = new CreateTransactionRequest(
                TransactionOperationType.TRANSFER.toString(), "source-id", "target-id", 150L);

        when(accountsDataService.get("source-id", false)).thenReturn(sourceAccount);
        when(accountsDataService.get("target-id", false)).thenReturn(targetAccount);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            createTransaction.execute(request);
        });

        assertEquals(ExceptionCode.NOT_FOUND_EXCEPTION, exception.getCode());
        assertEquals("source account does not have enough founds", exception.getMessage());
    }
}
