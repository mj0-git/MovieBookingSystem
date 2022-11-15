package org.booking.system.Service;

import org.booking.system.DTO.SecurityAccount;
import org.booking.system.Repo.SecurityRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service("securityAccountService")
public class SecurityAccountServiceImpl implements SecurityAccountService {

    @Autowired
    SecurityRepository securityRepository;
    @Autowired 
    private JWTUtil jwtUtil;

     @Override
    public String login(String username, String password) {
 
        System.out.println ("in SecurityAccountServiceImpl - method login .... " + " username = " + username +  "   password =  " + password);

        Optional<SecurityAccount> securityAccount = securityRepository.login(username,password);

        System.out.println("???????????????????????? securityAccount returned = " + securityAccount.toString());
        if(securityAccount.isPresent()){
            String tokenDB = UUID.randomUUID().toString();
            String jwtToken;
            try{
                //DO NOT SAVE JWT IN DB
                jwtToken = jwtUtil.generateToken(tokenDB);
            }catch(JWTCreationException e){
                //Issue with creating JWT Token
                //Errors before saving UUID to DB, so just return empty
                System.out.println("--------------- ERROR in making JWT");
                return StringUtils.EMPTY;
            }catch(IllegalArgumentException e2){
                //Issue with creating JWT Token
                //Errors before saving UUID to DB, so just return empty
                System.out.println("--------------- ERROR in making JWT");
                return StringUtils.EMPTY;

            }

                //Save uuid Token in DB
                SecurityAccount custom= securityAccount.get();
                custom.setToken(tokenDB);
                securityRepository.save(custom);

                System.out.println("%%%%%%%%%%%%%%%%%%%% token is set = " + tokenDB);

                return jwtToken;

        }

        return StringUtils.EMPTY;
    }

    @Override
    public Optional<User> findByToken(String jwttoken) {
        //Verify JST and recover unique string from jwtToken
        // Verify token and extract email
        try{
            String uniqueUserString = jwtUtil.validateTokenAndRetrieveSubject(jwttoken);

            Optional<SecurityAccount> securityAccount= securityRepository.findByToken(uniqueUserString);
            if(securityAccount.isPresent()){
                SecurityAccount securityAccountDB = securityAccount.get();
                User user= new User(securityAccountDB.getUserName(), securityAccountDB.getPassword(), true, true, true, true,
                        AuthorityUtils.createAuthorityList("USER"));
                return Optional.of(user);
            }
            return  Optional.empty();
        }catch(JWTVerificationException exc){
            return Optional.empty();
        }

        
    }

    @Override
    public SecurityAccount findById(Long id) {

        System.out.println("********************    " + "in SecurityAccountServiceImpl, findByID " + "id= " + id);
        Optional<SecurityAccount> securityAccount= securityRepository.findById(id);

        System.out.println("%%%%%%%%%%%%%  " + "securityAccount  = " + securityAccount.toString());
        
        return securityAccount.orElse(null);
    }

}