package jslozano.challenge6.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {
    // Information of the client
    // checking or savings
    private String typeOfAccount;
    private String firstName;
    private String lastName;
    private Long identityCard;
    private double accountBalance;
    // Information of the bank, its id and name
    private final String bankName;
    private final int id;
}
