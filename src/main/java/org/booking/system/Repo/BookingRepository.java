package org.booking.system.Repo;

import org.booking.system.DTO.ShowtimeDTO.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> getBookingByUserId(Long userId);
    List<Booking> getBookingByShowtimeId(Long showtimeId);
    //Optional<Booking> getBookingByUsernameAndShowtimeId(String username, Long showtimeId);
    Optional<Booking> getBookingByUserIdAndShowtimeId(Long userId, Long showtimeId);

}
