package jslozano.challenge6.generator;

import jslozano.challenge6.model.Account;

public interface AccountGenerator {
    Account generateSenderAccount();
    Account generateRecipientAccount();
}
