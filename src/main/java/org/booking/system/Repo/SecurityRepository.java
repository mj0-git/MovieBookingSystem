package org.booking.system.Repo;

import org.booking.system.DTO.SecurityAccount;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityRepository extends CrudRepository<SecurityAccount, Long> {

    @Query(value = "SELECT u FROM SecurityAccount u where u.userName = ?1 and u.password = ?2 ")

    Optional<SecurityAccount> login(String username,String password);

    Optional<SecurityAccount> findByToken(String token);

}