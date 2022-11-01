package org.booking.system.Repo;

import org.booking.system.DTO.ShowtimeDTO.Showtime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepository extends CrudRepository<Showtime, Long> {
}
