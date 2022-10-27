package org.booking.system.Service;

import org.booking.system.DTO.MovieDTO.Movie;

public interface TheaterService {
    Movie getMovie(String id, String title);
}
