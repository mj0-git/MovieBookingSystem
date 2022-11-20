package org.booking.system.Controller;

import org.booking.system.DTO.MovieDTO.Movie;
import org.booking.system.DTO.ShowtimeDTO.Booking;
import org.booking.system.DTO.ShowtimeDTO.Showtime;
import org.booking.system.Exception.BadRequestException;
import org.booking.system.Service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TheaterController {
    @Autowired
    private TheaterService theaterService;

    @RequestMapping(path= "/theater/movie",
            method= RequestMethod.GET,
            produces={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Movie> getMovieByID(@RequestParam(value = "id", required = false) String id,
                                              @RequestParam(value = "title", required = false) String title)
    {
        Movie movie = theaterService.getMovie(id, title);
        ResponseEntity<Movie> response = ResponseEntity.ok(movie);
        return response;
    }

    @RequestMapping(path= "/theater/admin/showtimes",
            method= RequestMethod.POST)
    public ResponseEntity createShowtime(@Valid @RequestBody Showtime showtime){
        theaterService.createShowtime(showtime);
        ResponseEntity<Showtime> response = ResponseEntity.status(HttpStatus.CREATED).build();
        return response;
    }
    @RequestMapping(path= "/theater/admin/showtimes/{id}",
            method= RequestMethod.DELETE)
    public ResponseEntity deleteShowtime(@PathVariable Long id){
        theaterService.deleteShowtime(id);
        ResponseEntity<Showtime> response = ResponseEntity.ok().build();
        return response;
    }
    @RequestMapping(path= "/theater/admin/showtimes/{id}/booking",
            method= RequestMethod.GET)
    public ResponseEntity<List<Booking>> getBookings(@PathVariable Long id){
        List<Booking> bookings = theaterService.getBookingByShowtimeId(id);
        ResponseEntity<List<Booking>> response = ResponseEntity.ok(bookings);
        return response;
    }

    @RequestMapping(path= "/theater/showtimes",
            method= RequestMethod.GET,
            produces={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Showtime>> getAllShowtime(){
        List<Showtime> showtimes = theaterService.getAllShowtimes();
        ResponseEntity<List<Showtime>> response = ResponseEntity.ok(showtimes);
        return response;
    }

    @RequestMapping(path= "/theater/showtimes/{id}",
            method= RequestMethod.GET,
            produces={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Showtime> getShowtime(@PathVariable Long id){
        Showtime showtime = theaterService.getShowtime(id);
        ResponseEntity<Showtime> response = ResponseEntity.ok(showtime);
        return response;
    }

    @RequestMapping(path= "/theater/showtimes/{id}/booking",
            method= RequestMethod.POST)
    public ResponseEntity addBooking(@PathVariable Long id,
                                     @Valid @RequestBody Booking booking){
        theaterService.addBooking(id, booking);
        ResponseEntity<Showtime> response = ResponseEntity.status(HttpStatus.OK).build();
        return response;
    }

    @RequestMapping(path= "/theater/showtimes/{id}/booking",
            method= RequestMethod.PUT)
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id,
                                     @Valid @RequestBody Booking booking){
        Booking update = theaterService.updateBooking(id, booking);
        ResponseEntity<Booking> response = ResponseEntity.ok(update);
        return response;
    }

    @RequestMapping(path= "/theater/showtimes/{id}/booking",
            method= RequestMethod.DELETE)
    public ResponseEntity deleteBooking(@PathVariable Long id,
                                        @RequestBody Booking booking){
        if (booking.userId()==null || booking.userId().isEmpty()){
            throw new BadRequestException("Missing username in request body");
        }
        theaterService.deleteBooking(id, booking);
        ResponseEntity<Showtime> response = ResponseEntity.status(HttpStatus.OK).build();
        return response;
    }

}