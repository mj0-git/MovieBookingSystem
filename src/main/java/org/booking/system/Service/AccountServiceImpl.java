package org.booking.system.Service;

import org.booking.system.DTO.Account;
import org.booking.system.Repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Save operation
    @Override
    public Account saveAccount(Account account)
    {
        return accountRepository.save(account);
    }

    // Read operation, return list
    @Override public List<Account> fetchAccountList()
    {
        return (List<Account>)
                accountRepository.findAll();
    }

    // Read operation, return single
    @Override public Optional<Account> fetchAccount(Long id)
    {
        return accountRepository.findById(id);
    }

    @Override
    public Optional<Account> fetchAccountByUsername(String username) {
        return accountRepository.getAccountByUserName(username);
    }

    // Update operation
    @Override
    public Account updateAccount(Account account,
                     Long accountId)
    {
        Account userDB
                = accountRepository.findById(accountId)
                .get();

        if (Objects.nonNull(account.getUserName())
                && !"".equalsIgnoreCase(
                account.getUserName())) {
            userDB.setUserName(
                    account.getUserName());
        }

        if (Objects.nonNull(account.getPassword())
                && !"".equalsIgnoreCase(
                account.getPassword())) {
            userDB.setPassword(
                    account.getPassword());
        }

        if (Objects.nonNull(account.getRole())
                && !"".equalsIgnoreCase(
                account.getRole())) {
            userDB.setRole(
                    account.getRole());
        }
        else{
            userDB.setRole("ROLE_USER");
        }
        if (Objects.nonNull(account.getFirstName())
                && !"".equalsIgnoreCase(
                account.getFirstName())) {
            userDB.setFirstName(
                    account.getFirstName());
        }
        if (Objects.nonNull(account.getLastName())
                && !"".equalsIgnoreCase(
                account.getLastName())) {
            userDB.setLastName(
                    account.getLastName());
        }

        if (Objects.nonNull(
                account.getAddress())
                && !"".equalsIgnoreCase(
                account.getAddress())) {
            userDB.setAddress(
                    account.getAddress());
        }

        if (Objects.nonNull(account.getEmail())
                && !"".equalsIgnoreCase(
                account.getEmail())) {
            userDB.setEmail(
                    account.getEmail());
        }

        if (Objects.nonNull(account.getPhoneNumber())
                && !"".equalsIgnoreCase(
                account.getPhoneNumber())) {
            userDB.setPhoneNumber(
                    account.getPhoneNumber());
        }

        if (Objects.nonNull(account.getFavorites())) {
            userDB.setFavorites(
                    account.getFavorites());
        }


        return accountRepository.save(userDB);
    }

    // Delete operation
    @Override
    public void deleteAccountById(Long accountId)
    {
        accountRepository.deleteById(accountId);
    }
}