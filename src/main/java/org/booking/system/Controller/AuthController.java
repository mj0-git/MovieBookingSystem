package org.booking.system.Controller;

import org.booking.system.DTO.Account;
import org.booking.system.Service.AccountService;
import org.booking.system.Service.SecurityAccountService;
import org.apache.commons.lang3.StringUtils;
import org.booking.system.Exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class AuthController {

    @Autowired
    private SecurityAccountService securityAccountService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private Validator helper;

    @PostMapping("/token")
    public String getToken(@RequestBody Account secureUser){

        String userName=secureUser.getUserName();
        String password=secureUser.getPassword();
       System.out.println("in TokenController - getToken    ....   " + userName + "      " + password) ;
       
       String token= securityAccountService.login(userName,password);

       System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!      " + "=         " + token);

       if(StringUtils.isEmpty(token)){
            throw new BadRequestException("Issues with JWT creation");
       }
       return token;
    }

    // Save operation
    @PostMapping("/register")
    public ResponseEntity saveAccount(
            @Valid @RequestBody Account user)
    {
        //Data pattern validation
        boolean emailGood=helper.matchEmailPattern(user.getEmail());
        boolean phoneGood=helper.matchPhoneNumberPattern(user.getPhoneNumber());

        if(emailGood && phoneGood) {
            Account userMade = accountService.saveAccount(user);

            URI location = ServletUriComponentsBuilder.fromUriString("/users/")
                    .path("/{id}")
                    .buildAndExpand(userMade.getUserId())
                    .toUri();

            return ResponseEntity.created(location).build();
        }
        else {
            throw new BadRequestException("Account Object contains invalid fields");
        }
    }
}
