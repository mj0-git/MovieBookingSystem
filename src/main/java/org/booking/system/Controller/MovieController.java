package org.booking.system.Controller;

import org.booking.system.DTO.MovieDTO.Movie;
import org.booking.system.Service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MovieController {
    @Autowired
    private TheaterService theaterService;

    @RequestMapping(path= "/movie",
            method= RequestMethod.GET,
            produces={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Movie> getMovieByID(@RequestParam(value = "id", required = false) String id,
                                              @RequestParam(value = "title", required = false) String title)
    {
        Movie movie = theaterService.getMovie(id, title);
        ResponseEntity<Movie> response = ResponseEntity.ok(movie);
        return response;
    }

}