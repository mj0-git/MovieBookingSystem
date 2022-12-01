package org.booking.system.Service;

import org.booking.system.DTO.MovieDTO.Movie;
import org.booking.system.DTO.ShowtimeDTO.Booking;
import org.booking.system.DTO.ShowtimeDTO.Showtime;

import java.util.List;

public interface TheaterService {
    Movie getMovie(String id, String title);
    List<Showtime> getAllShowtimes();
    Showtime getShowtime(Long id);
    Showtime createShowtime(Showtime showtime);
    Showtime updateShowtime(Long id, Showtime showtime);
    void deleteShowtime(Long id);
    void addBooking(Long id, Booking booking);
    Booking updateBooking(Long id, Booking booking);
    void deleteBooking(Long id, Booking booking);
    List<Booking> getBookingByShowtimeId(Long id);
    List<Booking> getBookingByUserId(Long userId);

}

