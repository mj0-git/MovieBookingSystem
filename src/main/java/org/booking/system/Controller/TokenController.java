package org.booking.system.Controller;

import org.booking.system.Service.SecurityAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TokenController {

    @Autowired
    private SecurityAccountService securityAccountService;

    @PostMapping("/token")
    public String getToken(@RequestParam("userName") final String userName, @RequestParam("password") final String password){
       
       System.out.println("in TokenController - getToken    ....   " + userName + "      " + password) ;
       
       String token= securityAccountService.login(userName,password);

       System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!      " + "=         " + token);

       if(StringUtils.isEmpty(token)){
           return "no token found";
       }
       return token;
    }
}
