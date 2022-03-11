package jslozano.challenge6.bankService;


import jslozano.challenge6.exception.InsufficientFundsException;
import jslozano.challenge6.exception.InvalidTargetFundsException;
import jslozano.challenge6.generator.AccountGenerator;
import jslozano.challenge6.model.Account;
import jslozano.challenge6.model.Result;
import jslozano.challenge6.service.WireTransfer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class WireTransferTest {
    @Mock
    private AccountGenerator generator;

    @InjectMocks
    private WireTransfer wireTransfer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInvalidWireTransferByNotHavingEnoughMoney(){
        Account senderAccount = Account.builder().accountBalance(30000).build();
        Account recipientAccount = Account.builder().bankName("bbva").build(); // Here we verify the charge of 3500


        Mockito.when(generator.generateSenderAccount()).thenReturn(senderAccount);

        Mockito.when(generator.generateRecipientAccount()).thenReturn(recipientAccount);

        InsufficientFundsException ex = Assertions.assertThrows(InsufficientFundsException.class,
                ()-> wireTransfer.wireMoney());
        Assertions.assertEquals(WireTransfer.INVALID_TRANSFER_FOR_NOT_ENOUGH_MONEY,
                ex.getMessage());
        Mockito.verify(generator, Mockito.times(1)).generateSenderAccount();
        Mockito.verify(generator, Mockito.times(1)).generateRecipientAccount();
    }
    @Test
    void testInvalidWireByExcessFundsInRecipientAccount(){
        Account recipientAccount = Account.builder().typeOfAccount("checking").
                accountBalance(5_900_000).build();
        String typeOfAccount = recipientAccount.getTypeOfAccount();

        Mockito.when(generator.generateRecipientAccount()).thenReturn(recipientAccount);


        InvalidTargetFundsException ex = Assertions.assertThrows(InvalidTargetFundsException.class,
                ()->wireTransfer.wireMoney());
        Assertions.assertEquals(WireTransfer.INVALID_TRANSFER_FOR_EXCESS_MONEY_IN_RECIPIENT_ACCOUNT,
                ex.getMessage());
        Assertions.assertEquals("checking", typeOfAccount);
        Mockito.verify(generator, Mockito.times(1)).generateRecipientAccount();
    }
    @Test
    void testValidTransaction(){
        Account senderAccount = Account.builder().accountBalance(200_000).build();
        Account recipientAccount = Account.builder().accountBalance(250_000).build();

        Mockito.when(generator.generateSenderAccount()).thenReturn(senderAccount);
        Mockito.when(generator.generateRecipientAccount()).thenReturn(recipientAccount);

        Result result = wireTransfer.wireMoney();

        Assertions.assertTrue(result.isStatus());

        Mockito.verify(generator, Mockito.times(1)).generateSenderAccount();
        Mockito.verify(generator, Mockito.times(1)).generateRecipientAccount();
    }
}
