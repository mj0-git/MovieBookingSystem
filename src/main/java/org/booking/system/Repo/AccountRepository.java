package org.booking.system.Repo;

import org.booking.system.DTO.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository
        extends CrudRepository<Account, Long> {
}