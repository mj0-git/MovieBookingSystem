package org.booking.system.Service;

import org.booking.system.DTO.SecurityAccount;
import org.booking.system.Repo.SecurityRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
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

     @Override
    public String login(String username, String password) {
 
        System.out.println ("in SecurityAccountServiceImpl - method login .... " + " username = " + username +  "   password =  " + password);

        Optional<SecurityAccount> securityAccount = securityRepository.login(username,password);

        System.out.println("???????????????????????? securityAccount returned = " + securityAccount.toString());

        if(securityAccount.isPresent()){
            String token = UUID.randomUUID().toString();
            SecurityAccount custom= securityAccount.get();
            custom.setToken(token);
            securityRepository.save(custom);

            System.out.println("%%%%%%%%%%%%%%%%%%%% token is set = " + token);

            return token;
        }

        return StringUtils.EMPTY;
    }

    @Override
    public Optional<User> findByToken(String token) {
        Optional<SecurityAccount> securityAccount= securityRepository.findByToken(token);
        if(securityAccount.isPresent()){
            SecurityAccount securityAccountDB = securityAccount.get();
            User user= new User(securityAccountDB.getUserName(), securityAccountDB.getPassword(), true, true, true, true,
                    AuthorityUtils.createAuthorityList("USER"));
            return Optional.of(user);
        }
        return  Optional.empty();
    }

    @Override
    public SecurityAccount findById(Long id) {

        System.out.println("********************    " + "in SecurityAccountServiceImpl, findByID " + "id= " + id);
        Optional<SecurityAccount> securityAccount= securityRepository.findById(id);

        System.out.println("%%%%%%%%%%%%%  " + "securityAccount  = " + securityAccount.toString());
        
        return securityAccount.orElse(null);
    }

}