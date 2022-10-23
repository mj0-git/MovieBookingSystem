package org.booking.system.Service;

import org.booking.system.DTO.Account;

import java.util.List;

// Interface
public interface AccountService {

    // Save operation
    Account saveAccount(Account account);

    // Read operation, return list
    List<Account> fetchAccountList();

    //Read operation, single object
    Account fetchAccount(Long id);

    // Update operation
    Account updateAccount(Account account,
                                Long accountId);

    // Delete operation
    void deleteAccountById(Long accountId);
}