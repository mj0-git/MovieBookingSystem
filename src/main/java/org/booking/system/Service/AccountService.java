package org.booking.system.Service;

import org.booking.system.DTO.Account;

import java.util.List;
import java.util.Optional;

// Interface
public interface AccountService {

    // Save operation
    Account saveAccount(Account account);

    // Read operation, return list
    List<Account> fetchAccountList();

    //Read operation, single object
    Optional<Account> fetchAccount(Long id);
    Optional<Account> fetchAccountByUsername (String username);

    // Update operation
    Account updateAccount(Account account,
                                Long accountId);

    // Delete operation
    void deleteAccountById(Long accountId);
}