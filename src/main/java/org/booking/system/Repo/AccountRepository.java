package org.booking.system.Repo;

import org.booking.system.DTO.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
     @Query(value = "SELECT u FROM Account u where u.userName = ?1 and u.password = ?2 ")

    Optional<Account> login(String username,String password);

    Optional<Account> findByToken(String token);

    Optional<Account> getAccountByUserName(String username);
}