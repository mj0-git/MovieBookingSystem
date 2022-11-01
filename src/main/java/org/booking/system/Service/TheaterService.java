package org.booking.system.Service;

import org.booking.system.DTO.MovieDTO.Movie;
import org.booking.system.DTO.ShowtimeDTO.Showtime;

import java.util.List;

public interface TheaterService {
    Movie getMovie(String id, String title);
    List<Showtime> getAllShowtimes();
    Showtime getShowtime(Long id);
    void createShowtime(Showtime showtime);
    void deleteShowtime(Long id);
}
