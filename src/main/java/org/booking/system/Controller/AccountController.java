package org.booking.system.Controller;

import org.booking.system.DTO.Account;
import org.booking.system.Service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.http.ResponseEntity;

import org.springframework.http.HttpHeaders;

import javax.validation.Valid;
import java.util.List;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

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
                        .buildAndExpand(userMade.getUserId())
                        .toUri();

        return ResponseEntity.created(location).build();
    }

    // Read operation, all users
    @GetMapping(value = "/users", produces = { "application/json", "application/xml" })
    public ResponseEntity<List<Account>> fetchAccountList(@RequestHeader("Content-Type") String contentType)
    {
        HttpHeaders headers = getHeaders(contentType);
        List<Account> userListDB = accountService.fetchAccountList();
        // if(userListDB.isPresent()){
        //     List<Account> userList = userListDB.get();
            return new ResponseEntity<>(userListDB,headers, HttpStatus.OK);
        // }
        // else{
        //     return new ResponseEntity<>(null, headers, HttpStatus.OK);
        // }
    }

    // Read operation
    @GetMapping(value ="/users/{accountId}", produces = { "application/json", "application/xml" })
    public ResponseEntity<Account> fetchAccount(@PathVariable("accountId") Long accountId,
        @RequestHeader("Content-Type") String contentType)
    {
        HttpHeaders headers = getHeaders(contentType);
        Optional<Account> userDB=accountService.fetchAccount(accountId);
        if(userDB.isPresent()){
            Account user = userDB.get();
            return new ResponseEntity<>(user,headers, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, headers, HttpStatus.OK);
        }
    }

    // Update operation
    @PutMapping(value = "/users/{accountId}", produces = { "application/json", "application/xml" })
    public ResponseEntity<Account> updateAccount(@RequestBody Account user, 
        @RequestHeader("Content-Type") String contentType, @PathVariable("accountId") Long accountId)
    {
        HttpHeaders headers = getHeaders(contentType);
        return new ResponseEntity<>(accountService.updateAccount(user, accountId),headers, HttpStatus.OK);
    }

    // Delete operation
    @DeleteMapping(value = "/users/{accountId}", produces= {"text/plain"})
    public ResponseEntity deleteAccountById(@PathVariable("accountId")
                                       Long accountId)
    {
        accountService.deleteAccountById(accountId);
        return ResponseEntity.ok("Account "+ accountId.toString()+" Deleted ");
    }

    //Add favorite
    @PutMapping(value = "/users/{accountId}/favorites/{movieId}", 
        produces = { "application/json", "application/xml" })
    public ResponseEntity<Account> updateFavoritesById(@RequestHeader("Content-Type") String contentType,
        @PathVariable("accountId") Long accountId, @PathVariable("movieId") Long movieId,
        @Valid @RequestBody Account userMade)
    {

        HttpHeaders headers = getHeaders(contentType);
        Optional<Account> userDB=accountService.fetchAccount(accountId);

        if(userDB.isPresent()){
            Account user = userDB.get();
            long[] favorites = userMade.getFavorites();
            long[] newFavs = Arrays.copyOf(favorites, favorites.length + 1);
            newFavs[favorites.length]=movieId;
            user.setFavorites(newFavs);

            return new ResponseEntity<>(
                accountService.updateAccount(user, accountId),headers, HttpStatus.OK
                );
        }
        else{
            return new ResponseEntity<>(null, headers, HttpStatus.OK);
        }
    }

    //Drop favorite
    @DeleteMapping(value = "/users/{accountId}/favorites/{movieId}", produces= {"text/plain"})
    public ResponseEntity<Account> deleteFavoriteById(@RequestHeader("Content-Type") String contentType,
        @PathVariable("accountId") Long accountId, @PathVariable("movieId") Long movieId,
        @Valid @RequestBody Account userMade)
    {
        HttpHeaders headers = getHeaders(contentType);
        Optional<Account> userDB=accountService.fetchAccount(accountId);
        if(userDB.isPresent()){
            Account user = userDB.get();
            long[] favorites = userMade.getFavorites();
            long[] newFavs = new long[favorites.length-1];
            for(int i=0, j=0; i<favorites.length; i++){
                if(favorites[i]!=movieId){
                    newFavs[j++]=favorites[i];
                }
            }
            user.setFavorites(newFavs);

            return new ResponseEntity<>(
                accountService.updateAccount(user, accountId),headers, HttpStatus.OK
                );
        }
        else{
            return new ResponseEntity<>(null, headers, HttpStatus.OK);
        }
    }

    //Get favorites
    @GetMapping(value ="/users/{accountId}/favorites", produces = { "application/json", "application/xml" })
    public ResponseEntity fetchFavorites(@PathVariable("accountId") Long accountId,
        @RequestHeader("Content-Type") String contentType, @Valid @RequestBody Account userMade)
    {
        HttpHeaders headers = getHeaders(contentType);
        Optional<Account> userDB=accountService.fetchAccount(accountId);
        if(userDB.isPresent()){
            long[] favorites = userMade.getFavorites();

            JSONPObject jsonObj = new JSONPObject("favorites",favorites);
            return ResponseEntity.status(HttpStatus.OK)
                            .headers(headers)
                            .body(jsonObj);
        }
        else{
            return new ResponseEntity<>(null, headers, HttpStatus.OK);
        }
    }
    //Get Booking (Read-Only)
    @GetMapping(value ="/users/{accountId}/bookings", produces = { "application/json", "application/xml" })
    public ResponseEntity fetchBookings(@PathVariable("accountId") Long accountId,
        @RequestHeader("Content-Type") String contentType, @Valid @RequestBody Account userMade)
    {
        HttpHeaders headers = getHeaders(contentType);
        Optional<Account> userDB=accountService.fetchAccount(accountId);
        if(userDB.isPresent()){
            long[] bookings = userMade.getBookings();

            JSONPObject jsonObj = new JSONPObject("bookings",bookings);
            return ResponseEntity.status(HttpStatus.OK)
                            .headers(headers)
                            .body(jsonObj);
        }
        else{
            return new ResponseEntity<>(null, headers, HttpStatus.OK);
        }
    }

    private HttpHeaders getHeaders(String contentType){
        HttpHeaders headers = new HttpHeaders();
        if(contentType.equals("application/json")){
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        else if(contentType.equals("application/xml")){
            headers.setContentType(MediaType.APPLICATION_XML);
        }
        else {
            headers.setContentType(MediaType.TEXT_PLAIN);
        }
        return headers;
    }
}