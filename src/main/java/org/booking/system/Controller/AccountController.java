package org.booking.system.Controller;

import org.booking.system.DTO.Account;
import org.booking.system.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.util.JSONPObject;

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
    public ResponseEntity saveAccount(
            @Valid @RequestBody Account user)
    {
        Account userMade = accountService.saveAccount(user);

        URI location =ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(userMade.getId())
                        .toUri();

        return ResponseEntity.created(location).build();

        return 
    }

    // Read operation, all users
    @GetMapping(value = "/users", produces = { "application/json", "application/xml" })
    public ResponseEntity fetchAccountList(@RequestHeader("Content-Type") String contentType)
    {
        return ResponseEntity.status(HttpStatus.OK)
                        .contentType(contentType)
                        .body(accountService.fetchAccountList());
    }

    // Read operation
    @GetMapping(value ="/users/{accountId}", produces = { "application/json", "application/xml" })
    public ResponseEntity fetchAccount(@PathVariable("accountId") Long accountId,
        @RequestHeader("Content-Type") String contentType)
    {
        return ResponseEntity.status(HttpStatus.OK)
                        .contentType(contentType)
                        .body(accountService.fetchAccount(accountId));
    }

    // Update operation
    @PutMapping(value = "/users/{accountId}", produces = { "application/json", "application/xml" })
    public ResponseEntity updateAccount(@RequestBody Account user, 
        @RequestHeader("Content-Type") String contentType, @PathVariable("accountId") Long accountId)
    {
        return ResponseEntity.status(HttpStatus.OK)
                        .contentType(contentType)
                        .body(accountService.updateAccount(
                                user, accountId));
    }

    // Delete operation
    @DeleteMapping(value = "/users/{accountId}", produces= {"text/plain"})
    public ResponseEntity deleteAccountById(@PathVariable("accountId")
                                       Long accountId)
    {
        accountService.deleteAccountById(
                accountId);
        return ResponseEntity.ok("Account "+ accountId.toString()+" Deleted ");
    }

    //Add favorite
    @PutMapping(value = "/users/{accountId}/favorites/{movieId}", 
        produces = { "application/json", "application/xml" })
    public ResponseEntity updateFavoritesById(@RequestHeader("Content-Type") String contentType,
        @PathVariable("accountId") Long accountId, @PathVariable("movieId") Long movieId)
    {
        Account userDB=accountService.fetchAccount(accountId);

        long[] favorites = userMade.getFavorites();
        long[] newFavs = Arrays.copyOf(favorites, favorites.length + 1);
        newFavs[favorites.size()]=movieId;
        userDB.setFavorites(newFavs);

        return ResponseEntity.status(HttpStatus.OK)
                        .contentType(contentType)
                        .body(accountService.updateAccount(
                                userDB, accountId));
    }

    //Drop favorite
    @DeleteMapping(value = "/users/{accountId}/favorites/{movieId}", produces= {"text/plain"})
    public ResponseEntity deleteFavoriteById(@RequestHeader("Content-Type") String contentType,
        @PathVariable("accountId") Long accountId, @PathVariable("movieId") Long movieId)
    {
        Account userDB=accountService.fetchAccount(accountId);

        long[] favorites = userMade.getFavorites();
        long[] newFavs = new long[favorites.length-1];
        for(int i=0, j=0; i<favorites.length; i++){
            if(favorites[i]!=movieId){
                newFavs[j++]=favorites[i];
            }
        }
        userDB.setFavorites(newFaves)
        return ResponseEntity.status(HttpStatus.OK)
                        .contentType(contentType)
                        .body(accountService.updateAccount(
                                userDB, accountId));
    }

    //Get favorites
    @GetMapping(value ="/users/{accountId}/favorites", produces = { "application/json", "application/xml" })
    public ResponseEntity fetchFavorites(@PathVariable("accountId") Long accountId,
        @RequestHeader("Content-Type") String contentType)
    {
        Account userDB=accountService.fetchAccount(accountId);

        long[] favorites = userMade.getFavorites();

        JSONPObject jsonObj = new JSONPObject("favorites",favorites);
        return ResponseEntity.status(HttpStatus.OK)
                        .contentType(contentType)
                        .body(jsonObj);
    }
    //Get Booking (Read-Only)
    @GetMapping(value ="/users/{accountId}/bookings", produces = { "application/json", "application/xml" })
    public ResponseEntity fetchBookings(@PathVariable("accountId") Long accountId,
        @RequestHeader("Content-Type") String contentType)
    {
        Account userDB=accountService.fetchAccount(accountId);

        long[] bookings = userMade.getBookings();

        JSONPObject jsonObj = new JSONPObject("bookings",bookings);
        return ResponseEntity.status(HttpStatus.OK)
                        .contentType(contentType)
                        .body(jsonObj);
    }
}