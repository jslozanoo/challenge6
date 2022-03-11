package jslozano.challenge6.service;

import jslozano.challenge6.exception.InsufficientFundsException;
import jslozano.challenge6.exception.InvalidBillException;
import jslozano.challenge6.generator.AccountGenerator;
import jslozano.challenge6.generator.BillGenerator;
import jslozano.challenge6.model.Account;
import jslozano.challenge6.model.Bill;
import jslozano.challenge6.model.Result;
import org.springframework.beans.factory.annotation.Autowired;


public class OnlinePayments {

    public String INVALID_PAYMENT_FOR_NOT_ENOUGH_MONEY = "Account does not have enough money";
    public String INVALID_PAYMENT_FOR_INVALID_ID = "id does not have the requirements";

    @Autowired
    private BillGenerator billGenerator;

    @Autowired
    private AccountGenerator accountGenerator;

    public Result payBill(){
        Account account = accountGenerator.generateSenderAccount();
        Bill bill = billGenerator.generateBill();
        boolean status;

        double billValue = bill.getBillValue();
        billValue = this.billWithDiscountForCheckingAccount(account, billValue, 0.10);

        if(!this.hasAccountEnoughMoney(account, billValue , 0.20)){
            throw new InsufficientFundsException(INVALID_PAYMENT_FOR_NOT_ENOUGH_MONEY);
        }
        if(!(this.twoFirstDigitsAreZero(bill.getIdBill()) &&
                this.billLengthEqualsSeven(bill.getIdBill()))){
            throw new InvalidBillException(INVALID_PAYMENT_FOR_INVALID_ID);
        }
        status = true;
        this.onlinePaymentWithAccount(account, billValue);
        return Result.builder().status(status).numberOfTransaction(1L)
                .amountOfMoneyTransferred(billValue).build();
    }

    /***
     *
     * @param account Account of the client
     * @param billValue Value of the bill
     * @param additionalPercentage Additional percentage of the bill that must be in the account
     * @return true if the account has enough money, false otherwise
     */
    private boolean hasAccountEnoughMoney(Account account, double billValue,
                                          double additionalPercentage){
        return account.getAccountBalance() >= billValue * (1 + additionalPercentage);
    }
    /***
     * Discount a specific percentage of the bill for checking accounts
     * @param account Account of the client
     * @param billValue value of the bill
     * @param percentageOfDiscount Discount of the bill
     * @return the value of the bill with the discount if applies
     */
    private double billWithDiscountForCheckingAccount(Account account, double billValue,
                                                      double percentageOfDiscount){
        if (account.getTypeOfAccount() == null){
            return billValue;
        }
        if(account.getTypeOfAccount().equals("checking")){
            return billValue * (1 - percentageOfDiscount);
        }else {
            return billValue;
        }
    }
    private boolean billLengthEqualsSeven(String id){
        return id.length() == 7;
    }
    private boolean twoFirstDigitsAreZero(String id) {
        String[] idArray =id.split("");
        return idArray[0].equals("0") && idArray[1].equals("0");
    }
    private void onlinePaymentWithAccount(Account account, double billValue){
        account.setAccountBalance(account.getAccountBalance() - billValue);
    }

}
