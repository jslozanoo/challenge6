package jslozano.challenge6.service;

import jslozano.challenge6.exception.InsufficientFundsException;
import jslozano.challenge6.exception.InvalidTargetFundsException;
import jslozano.challenge6.generator.AccountGenerator;
import jslozano.challenge6.model.Account;
import jslozano.challenge6.model.Result;
import org.springframework.beans.factory.annotation.Autowired;

public class WireTransfer {
    public static String INVALID_TRANSFER_FOR_NOT_ENOUGH_MONEY = "Sender account does not have " +
            "enough money";
    public static String INVALID_TRANSFER_FOR_EXCESS_MONEY_IN_RECIPIENT_ACCOUNT = "Recipient " +
            "account has more than three times the money to be transferred";
    @Autowired
    private AccountGenerator accountGenerator; // Recipient and sender accounts

    public Result wireMoney(){
        Account recipientAccount = accountGenerator.generateRecipientAccount();
        Account senderAccount =accountGenerator.generateSenderAccount();
        double moneyToBeWired = 100_000;
        boolean status;


        if (!(recipientAccount.getBankName() == null)){
            if(!recipientAccount.getBankName().equals("globant")){
                this.chargeForWireBankDifferentToGlobant(senderAccount);
            }
        }
        if(moneyToBeWired > 1500000){
            moneyToBeWired *= 0.97;
        }

        if (!this.HasSenderAccountEnoughMoney(senderAccount, moneyToBeWired)){
            throw new InsufficientFundsException(INVALID_TRANSFER_FOR_NOT_ENOUGH_MONEY);
        }
        if(this.HasRecipientMuchMoney(recipientAccount, moneyToBeWired)){
            throw new InvalidTargetFundsException(INVALID_TRANSFER_FOR_EXCESS_MONEY_IN_RECIPIENT_ACCOUNT);
        }
        status = true;
        this.transferMoneyFromSenderToRecipient(senderAccount, recipientAccount, moneyToBeWired);
        // The number of Transaction must be generated for each transaction, for this purpose I set it
        return Result.builder().amountOfMoneyTransferred(moneyToBeWired).status(status).
                numberOfTransaction(1L).build();
    }

    /***
     *
     * @param senderAccount Account of the person who is sending money
     * @param transferAmount Amount of money to be transferred
     * @return true if there is enough money in the account for transfer that
     * amount of money, false otherwise
     */
    private boolean HasSenderAccountEnoughMoney(Account senderAccount, double transferAmount){
        if (senderAccount == null){
            return true;
        }
        else {
            return senderAccount.getAccountBalance() >= transferAmount;
        }
    }

    /***
     *
     * @param recipientAccount Account of the person who is receiving the money, has to be
     *                         a checking account
     * @param transferAmount Amount of money to be transferred
     * @return true if the recipient account has more than three times the money to be transferred
     */
    private boolean HasRecipientMuchMoney(Account recipientAccount, double transferAmount){
        if (recipientAccount == null){
            return false;
        }
        return recipientAccount.getAccountBalance() > transferAmount * 3;
    }

    private void chargeForWireBankDifferentToGlobant(Account senderAccount){
        senderAccount.setAccountBalance(senderAccount.getAccountBalance() - 3_500);

    }
    private void transferMoneyFromSenderToRecipient(Account senderAccount, Account recipientAccount,
                           double amountMoney){
        senderAccount.setAccountBalance(senderAccount.getAccountBalance() - amountMoney);
        recipientAccount.setAccountBalance(recipientAccount.getAccountBalance() + amountMoney);
    }

}
