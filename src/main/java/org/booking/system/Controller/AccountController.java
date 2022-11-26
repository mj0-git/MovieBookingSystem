package org.booking.system.Controller;

import org.booking.system.DTO.Account;
import org.booking.system.DTO.ShowtimeDTO.Booking;
import org.booking.system.Service.AccountService;
import org.booking.system.Service.TheaterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.booking.system.DTO.MovieDTO.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.http.ResponseEntity;
import org.booking.system.Exception.BadRequestException;
import org.booking.system.Exception.NotFoundException;

import org.springframework.http.HttpHeaders;

import javax.validation.Valid;
import java.util.List;
import java.util.ArrayList;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private TheaterService theaterService;

    @Autowired
    private Validator validator;

    @GetMapping(value = "/users/test", produces= {"text/plain"})
    public ResponseEntity testAccount(){
        return ResponseEntity.ok("Testing getAccountService method annotated with @GET");
    }


    // Read operation, all users
    @GetMapping(value = "/users", produces = { "application/json", "application/xml" })
    public ResponseEntity<List<Account>> fetchAccountList(@RequestHeader(value = "Content-Type", required = false) String contentType)
    {
        HttpHeaders headers = validator.getHeaders(contentType);
        List<Account> userListDB = accountService.fetchAccountList();
        return new ResponseEntity<>(userListDB,headers, HttpStatus.OK);
    }

    // Read operation
    @GetMapping(value ="/users/{accountId}", produces = { "application/json", "application/xml" })
    public ResponseEntity<Account> fetchAccount(@PathVariable("accountId") Long accountId,
        @RequestHeader("Content-Type") String contentType)
    {
        HttpHeaders headers = validator.getHeaders(contentType);
        Optional<Account> userDB=accountService.fetchAccount(accountId);
        if(userDB.isPresent()){
            Account user = userDB.get();
            return new ResponseEntity<>(user,headers, HttpStatus.OK);
        } else{
            throw new NotFoundException(String.format("No user found in database with id=%s", accountId));
        }
    }

    // Update operation
    @PutMapping(value = "/users/{accountId}", produces = { "application/json", "application/xml" })
    public ResponseEntity<Account> updateAccount(@RequestBody Account user,
        @RequestHeader("Content-Type") String contentType, @PathVariable("accountId") Long accountId)
    {
        //Data pattern validation
        boolean emailGood=validator.matchEmailPattern(user.getEmail());
        boolean phoneGood=validator.matchPhoneNumberPattern(user.getPhoneNumber());
        HttpHeaders headers = validator.getHeaders(contentType);

        Optional<Account> userDB=accountService.fetchAccount(accountId);

        if(userDB.isPresent()){
            if(emailGood && phoneGood) {
                return new ResponseEntity<>(accountService.updateAccount(user, accountId),headers, HttpStatus.OK);
            }
            else {
                throw new BadRequestException("Email or Phone contains invalid pattern");
            } 
        } else{
            throw new NotFoundException(String.format("No user found in database with id=%s", accountId));
        }
    }

    // Delete operation
    @DeleteMapping(value = "/users/{accountId}", produces= {"text/plain"})
    public ResponseEntity deleteAccountById(@PathVariable("accountId")
                                       Long accountId)
    {
        Optional<Account> userDB=accountService.fetchAccount(accountId);

        if(userDB.isPresent()){
            accountService.deleteAccountById(accountId);
            return ResponseEntity.ok("Account "+ accountId.toString()+" Deleted ");
        } else{
            throw new NotFoundException(String.format("No user found in database with id=%s", accountId));
        }
    }

    //Add favorite
    @PutMapping(value = "/users/{accountId}/favorites/{movieId}", 
        produces = { "application/json", "application/xml" })
    public ResponseEntity<Account> updateFavoritesById(@RequestHeader("Content-Type") String contentType,
        @PathVariable("accountId") Long accountId, @PathVariable("movieId") String movieId)
    {

        HttpHeaders headers = validator.getHeaders(contentType);
        Optional<Account> userDB=accountService.fetchAccount(accountId);

        //Movie movie = theaterService.getMovie(movieId, "");

        if(userDB.isPresent()){
            Account user = userDB.get();
            List<String> favorites = user.getFavorites();
            if(favorites.size()<=0){
                favorites=new ArrayList<String>();
            }
            boolean found=false;
            if(favorites.contains(movieId)){
                found=true;
            }

            if(found){
                //No need to duplicate if list does contain, so return
                System.out.println("Duplicate");
                return new ResponseEntity<>(user,headers, HttpStatus.OK);
            }
            ArrayList<String> newFavs = new ArrayList<String>(favorites);
            newFavs.add(movieId);
            user.setFavorites(newFavs);

            return new ResponseEntity<>(
                accountService.updateAccount(user, accountId),headers, HttpStatus.OK
                );
        }
        else{
            throw new NotFoundException(String.format("No user found in database with id=%s", accountId));
        }
    }

    //Drop favorite
    @DeleteMapping(value = "/users/{accountId}/favorites/{movieId}", produces= {"text/plain"})
    public ResponseEntity<Account> deleteFavoriteById(@RequestHeader("Content-Type") String contentType,
        @PathVariable("accountId") Long accountId, @PathVariable("movieId") String movieId)
    {
        HttpHeaders headers = validator.getHeaders(contentType);
        Optional<Account> userDB=accountService.fetchAccount(accountId);
        if(userDB.isPresent()){
            Account user = userDB.get();
            ArrayList<String> favorites = new ArrayList<String>(user.getFavorites());
            if(favorites.size()<=0){
                //No need to delete if empty list 
                System.out.println("Favorites null");
                return new ResponseEntity<>(user,headers, HttpStatus.OK);
            }

            if(!favorites.contains(movieId)){
                System.out.println("Does not contain");
                return new ResponseEntity<>(user,headers, HttpStatus.OK);
            }

            int i=0;
            for(String fav:favorites){
                if(fav.equals(movieId)){
                    break;
                }
                i++;
            }

            favorites.remove(i);
            user.setFavorites(favorites);

            return new ResponseEntity<>(
                accountService.updateAccount(user, accountId),headers, HttpStatus.OK
                );
        }
        else{
            throw new NotFoundException(String.format("No user found in database with id=%s", accountId));
        }
    }

    //Get favorites
    @GetMapping(value ="/users/{accountId}/favorites", produces = { "application/json", "application/xml" })
    public ResponseEntity<List<String>> fetchFavorites(@PathVariable("accountId") Long accountId,
        @RequestHeader("Content-Type") String contentType)
    {
        HttpHeaders headers = validator.getHeaders(contentType);
        Optional<Account> userDB=accountService.fetchAccount(accountId);
        if(userDB.isPresent()){
            Account user = userDB.get();
            List<String> favorites = user.getFavorites();
            if(favorites.size()<=0){
                favorites=new ArrayList<String>();
            }
            return ResponseEntity.status(HttpStatus.OK)
                            .headers(headers)
                            .body(favorites);
        }
        else{
            throw new NotFoundException(String.format("No user found in database with id=%s", accountId));
        }
    }
    //Get Booking (Read-Only)
    @GetMapping(value ="/users/{accountId}/bookings", produces = { "application/json", "application/xml" })
    public ResponseEntity<List<Booking>> fetchBookings(@PathVariable("accountId") Long accountId,
        @RequestHeader("Content-Type") String contentType)
    {
        HttpHeaders headers = validator.getHeaders(contentType);
        Optional<Account> userDB=accountService.fetchAccount(accountId);
        if(userDB.isPresent()){
            List<Booking> bookings = theaterService.getBookingByUserId(accountId);

            return ResponseEntity.status(HttpStatus.OK)
                            .headers(headers)
                            .body(bookings);
        } else{
            throw new NotFoundException(String.format("No user found in database with id=%s", accountId));
        }
    }

}