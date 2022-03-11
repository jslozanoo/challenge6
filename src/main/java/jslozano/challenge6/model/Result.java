package jslozano.challenge6.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result {
    private boolean status;
    private double amountOfMoneyTransferred;
    private Long numberOfTransaction;

    public boolean isStatus() {
        return status;
    }





}
