package org.booking.system.Controller;

import org.booking.system.DTO.Account;
import org.booking.system.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping(value = "/users/test", produces= {"text/plain"})
    public ResponseEntity testAccount(){
        return ResponseEntity.ok("Testing getAccountService method annotated with @GET");
    }

    // Save operation
    @PostMapping("/users")
    public Account saveAccount(
            @Valid @RequestBody Account user)
    {
        Account userMade = accountService.saveAccount(user);

        URI location =ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(student.getId())
                        .toUri();

        return ResponseEntity.created(location).build();

        return 
    }

    // Read operation, all users
    @GetMapping("/users")
    public List<Account> fetchAccountList()
    {
        return accountService.fetchAccountList();
    }

    // Read operation
    @GetMapping("/users/{id}")
    public Account fetchAccount(@PathVariable("id") Long accountId)
    {
        return accountService.fetchAccount(accountId);
    }

    // Update operation
    @PutMapping("/users/{id}")
    public Account
    updateAccount(@RequestBody Account user,
                     @PathVariable("id") Long accountId)
    {
        return accountService.updateAccount(
                user, accountId);
    }

    // Delete operation
    @DeleteMapping("/users/{id}")
    public String deleteAccounttById(@PathVariable("id")
                                       Long accountId)
    {
        accountService.deleteAccountById(
                accountId);
        return "Deleted Successfully";
    }
}