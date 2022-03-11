package jslozano.challenge6.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Bill {
    private int idCompany;
    private String idBill; // Can't be int because the two first digits must be zero
    private String expirationDate; //Format yyyy-mm-dd
    private double billValue;
}
