package jslozano.challenge6.paymentsService;


import com.sun.source.tree.AssertTree;
import jslozano.challenge6.exception.InsufficientFundsException;
import jslozano.challenge6.exception.InvalidBillException;
import jslozano.challenge6.generator.AccountGenerator;
import jslozano.challenge6.generator.BillGenerator;
import jslozano.challenge6.model.Account;
import jslozano.challenge6.model.Bill;
import jslozano.challenge6.model.Result;
import jslozano.challenge6.service.OnlinePayments;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class OnlinePaymentsTest {
    @Mock
    BillGenerator billGenerator;

    @Mock
    AccountGenerator accountGenerator;

    @InjectMocks
    OnlinePayments onlinePayments;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInvalidPaymentByNotHavingEnoughMoney(){
        Account account = Account.builder().accountBalance(100).
                typeOfAccount("checking").build();
        Bill bill= Bill.builder().billValue(100).build();

        Mockito.when(accountGenerator.generateSenderAccount()).thenReturn(account);
        Mockito.when(billGenerator.generateBill()).thenReturn(bill);

        InsufficientFundsException ex = Assertions.assertThrows(InsufficientFundsException.class,
                ()->onlinePayments.payBill());
        Assertions.assertEquals(onlinePayments.INVALID_PAYMENT_FOR_NOT_ENOUGH_MONEY,
                ex.getMessage());
        Mockito.verify(accountGenerator, Mockito.times(1)).generateSenderAccount();
        Mockito.verify(billGenerator, Mockito.times(1)).generateBill();
    }
    @Test
    void testInvalidByInvalidBillId(){
        Account account = Account.builder().build();
        Bill bill= Bill.builder().idBill("0623456").build();

        Mockito.when(accountGenerator.generateSenderAccount()).thenReturn(account);
        Mockito.when(billGenerator.generateBill()).thenReturn(bill);

        InvalidBillException ex = Assertions.assertThrows(InvalidBillException.class,
                ()->onlinePayments.payBill());
        Assertions.assertEquals(onlinePayments.INVALID_PAYMENT_FOR_INVALID_ID, ex.getMessage());
        Mockito.verify(accountGenerator, Mockito.times(1)).generateSenderAccount();
        Mockito.verify(billGenerator, Mockito.times(1)).generateBill();
    }
    // Only left implement the functionality, i.e, the online payment itself, like in the wire
    // transfer where we do in fact the transfer
    @Test
    void testValidPayment(){
        Account account = Account.builder().accountBalance(150).build();
        Bill bill= Bill.builder().billValue(100).idBill("0012345").build();

        Mockito.when(accountGenerator.generateSenderAccount()).thenReturn(account);
        Mockito.when(billGenerator.generateBill()).thenReturn(bill);

        Result result = onlinePayments.payBill();

        Assertions.assertTrue(result.isStatus());

        Mockito.verify(accountGenerator, Mockito.times(1)).generateSenderAccount();
        Mockito.verify(billGenerator, Mockito.times(1)).generateBill();


    }

}
