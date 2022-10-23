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
    @GetMapping(value = "/users", produces = { "application/json", "application/xml" })
    public List<Account> fetchAccountList(@RequestHeader("Content-Type") String contentType)
    {
        return ResponseEntity.status(HttpStatus.OK)
                        .contentType(contentType)
                        .body(accountService.fetchAccountList());
    }

    // Read operation
    @GetMapping(value ="/users/{id}", produces = { "application/json", "application/xml" })
    public Account fetchAccount(@PathVariable("id") Long accountId,
        @RequestHeader("Content-Type") String contentType)
    {
        return ResponseEntity.status(HttpStatus.OK)
                        .contentType(contentType)
                        .body(accountService.fetchAccount(accountId));
    }

    // Update operation
    @PutMapping(value = "/users/{id}", produces = { "application/json", "application/xml" })
    public Account
    updateAccount(@RequestBody Account user, @RequestHeader("Content-Type") String contentType,
                     @PathVariable("id") Long accountId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                        .contentType(contentType)
                        .body(accountService.updateAccount(
                                user, accountId));
    }

    // Delete operation
    @DeleteMapping(value = "/users/{id}", produces= {"text/plain"})
    public ResponseEntity deleteAccounttById(@PathVariable("id")
                                       Long accountId)
    {
        accountService.deleteAccountById(
                accountId);
        return ResponseEntity.ok("Account "+ accountId.toString()+" Deleted ");
    }

    //
}