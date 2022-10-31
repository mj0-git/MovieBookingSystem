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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        //Data pattern validation
        boolean emailGood=matchEmailPattern(user.getEmail());
        boolean phoneGood=matchPhoneNumberPattern(user.getPhoneNumber());

        if(emailGood && phoneGood) {
            Account userMade = accountService.saveAccount(user);

            URI location =ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(userMade.getUserId())
                        .toUri();

            return ResponseEntity.created(location).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account Object contains invalid fields");
        }        
    }

    // Read operation, all users
    @GetMapping(value = "/users", produces = { "application/json", "application/xml" })
    public ResponseEntity<List<Account>> fetchAccountList(@RequestHeader("Content-Type") String contentType)
    {
        HttpHeaders headers = getHeaders(contentType);
        List<Account> userListDB = accountService.fetchAccountList();
        return new ResponseEntity<>(userListDB,headers, HttpStatus.OK);
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
            return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
        }
    }

    // Update operation
    @PutMapping(value = "/users/{accountId}", produces = { "application/json", "application/xml" })
    public ResponseEntity<Account> updateAccount(@RequestBody Account user, 
        @RequestHeader("Content-Type") String contentType, @PathVariable("accountId") Long accountId)
    {
        //Data pattern validation
        boolean emailGood=matchEmailPattern(user.getEmail());
        boolean phoneGood=matchPhoneNumberPattern(user.getPhoneNumber());
        HttpHeaders headers = getHeaders(contentType);

        if(emailGood && phoneGood) {
            return new ResponseEntity<>(accountService.updateAccount(user, accountId),headers, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST);
        } 
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
        @PathVariable("accountId") Long accountId, @PathVariable("movieId") Long movieId)
    {

        HttpHeaders headers = getHeaders(contentType);
        Optional<Account> userDB=accountService.fetchAccount(accountId);

        if(userDB.isPresent()){
            Account user = userDB.get();
            long[] favorites = user.getFavorites();
            if(favorites==null){
                favorites=new long[0];
            }
            boolean found=false;
            for(long fav:favorites){
                if(movieId==fav){
                    found=true;
                }
            }

            if(found){
                //No need to duplicate if list does contain, so return
                System.out.println("Duplicate");
                return new ResponseEntity<>(user,headers, HttpStatus.OK);
            }
            long[] newFavs = Arrays.copyOf(favorites, favorites.length + 1);
            newFavs[favorites.length]=movieId;
            user.setFavorites(newFavs);

            return new ResponseEntity<>(
                accountService.updateAccount(user, accountId),headers, HttpStatus.OK
                );
        }
        else{
            return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
        }
    }

    //Drop favorite
    @DeleteMapping(value = "/users/{accountId}/favorites/{movieId}", produces= {"text/plain"})
    public ResponseEntity<Account> deleteFavoriteById(@RequestHeader("Content-Type") String contentType,
        @PathVariable("accountId") Long accountId, @PathVariable("movieId") Long movieId)
    {
        HttpHeaders headers = getHeaders(contentType);
        Optional<Account> userDB=accountService.fetchAccount(accountId);
        if(userDB.isPresent()){
            Account user = userDB.get();
            long[] favorites = user.getFavorites();
            if(favorites==null){
                //No need to delete if empty list 
                System.out.println("Favorites null");
                return new ResponseEntity<>(user,headers, HttpStatus.OK);
            }

            boolean not_found=true;
            for(long fav:favorites){
                if(movieId==fav){
                    not_found=false;
                }
            }

            if(not_found){
                //No need to delete if list does not contain, so return
                System.out.println("Does not contain");
                return new ResponseEntity<>(user,headers, HttpStatus.OK);
            }
            long[] newFavs=null;
            if(favorites.length<=1){
                newFavs=new long[]{};
                System.out.println("Favorites length<=1");
            }
            else{
                //If non-zero, make copy. If zero, just add null
                newFavs = new long[favorites.length-1];            
                for(int i=0, j=0; i<favorites.length; i++){
                    if(favorites[i]!=movieId){
                        newFavs[j++]=favorites[i];
                    }
                }
                System.out.println("Favorites length>1");
            }
            user.setFavorites(newFavs);

            return new ResponseEntity<>(
                accountService.updateAccount(user, accountId),headers, HttpStatus.OK
                );
        }
        else{
            return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
        }
    }

    //Get favorites
    @GetMapping(value ="/users/{accountId}/favorites", produces = { "application/json", "application/xml" })
    public ResponseEntity<long[]> fetchFavorites(@PathVariable("accountId") Long accountId,
        @RequestHeader("Content-Type") String contentType)
    {
        HttpHeaders headers = getHeaders(contentType);
        Optional<Account> userDB=accountService.fetchAccount(accountId);
        if(userDB.isPresent()){
            Account user = userDB.get();
            long[] favorites = user.getFavorites();
            if(favorites==null){
                favorites=new long[0];
            }
            return ResponseEntity.status(HttpStatus.OK)
                            .headers(headers)
                            .body(favorites);
        }
        else{
            return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
        }
    }
    //Get Booking (Read-Only)
    @GetMapping(value ="/users/{accountId}/bookings", produces = { "application/json", "application/xml" })
    public ResponseEntity<long[]> fetchBookings(@PathVariable("accountId") Long accountId,
        @RequestHeader("Content-Type") String contentType)
    {
        HttpHeaders headers = getHeaders(contentType);
        Optional<Account> userDB=accountService.fetchAccount(accountId);
        if(userDB.isPresent()){
            Account user = userDB.get();
            long[] bookings = user.getBookings();
            if(bookings==null){
                bookings=new long[0];
            }

            return ResponseEntity.status(HttpStatus.OK)
                            .headers(headers)
                            .body(bookings);
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
    private boolean matchEmailPattern(String email){
        if(email==null || email.trim().equals("")){
            return true;
        }
        //check email is valid
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);
        return mat.matches();
    }
    private boolean matchPhoneNumberPattern(String phone){
        if(phone==null || phone.trim().equals("")){
            return true;
        }
        //check email is valid
        Pattern pattern = Pattern.compile("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$");
        Matcher mat = pattern.matcher(phone);
        return mat.matches();
    }
}