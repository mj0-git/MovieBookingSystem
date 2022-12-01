package org.booking.system.Controller;

import org.booking.system.DTO.MovieDTO.Movie;
import org.booking.system.DTO.ShowtimeDTO.Booking;
import org.booking.system.DTO.ShowtimeDTO.Showtime;
import org.booking.system.Exception.BadRequestException;
import org.booking.system.Exception.NotFoundException;
import org.booking.system.Service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TheaterController {
    @Autowired
    private TheaterService theaterService;

    @Autowired
    private Validator validator;

    @RequestMapping(path= "/theater/movie",
            method= RequestMethod.GET,
            produces = { "application/json", "application/xml" })
    public ResponseEntity<Movie> getMovieByID(@RequestHeader(value = "Content-Type", required = false) String contentType,
                                              @RequestParam(value = "id", required = false) String id,
                                              @RequestParam(value = "title", required = false) String title)
    {
        HttpHeaders headers = validator.getHeaders(contentType);
        Movie movie = theaterService.getMovie(id, title);
        return new ResponseEntity<>(movie,headers, HttpStatus.OK);
    }

    @RequestMapping(path= "/theater/admin/showtimes",
            method= RequestMethod.POST)
    public ResponseEntity createShowtime(@Valid @RequestBody Showtime showtime, Errors errors){
        validator.validateBody(errors);
        Showtime created = theaterService.createShowtime(showtime);
        URI location = ServletUriComponentsBuilder.fromUriString("/showtimes")
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(path= "/theater/admin/showtimes/{id}",
            method= RequestMethod.PUT)
    public ResponseEntity updateShowtime(@RequestHeader(value = "Content-Type", required = false) String contentType,
                                         @PathVariable Long id,
                                         @Valid @RequestBody Showtime showtime, Errors errors){
        validator.validateBody(errors);
        HttpHeaders headers = validator.getHeaders(contentType);
        Showtime update = theaterService.updateShowtime(id, showtime);
        return new ResponseEntity<>(update,headers, HttpStatus.OK);
    }

    @RequestMapping(path= "/theater/admin/showtimes/{id}",
            method= RequestMethod.DELETE)
    public ResponseEntity deleteShowtime(@PathVariable Long id){
        theaterService.deleteShowtime(id);
        ResponseEntity<Showtime> response = ResponseEntity.ok().build();
        return response;
    }
    @RequestMapping(path= "/theater/admin/showtimes/{id}/bookings",
            method= RequestMethod.GET,
            produces = { "application/json", "application/xml" })
    public ResponseEntity<List<Booking>> getBookings(@RequestHeader(value = "Content-Type", required = false) String contentType,
                                                     @PathVariable Long id){
        HttpHeaders headers = validator.getHeaders(contentType);
        List<Booking> bookings = theaterService.getBookingByShowtimeId(id);
        return new ResponseEntity<>(bookings,headers, HttpStatus.OK);
    }

    @RequestMapping(path= "/theater/showtimes",
            method= RequestMethod.GET,
            produces = { "application/json", "application/xml" })
    public ResponseEntity<List<Showtime>> getAllShowtime(@RequestHeader(value = "Content-Type", required = false) String contentType){
        HttpHeaders headers = validator.getHeaders(contentType);
        List<Showtime> showtimes = theaterService.getAllShowtimes();
        return new ResponseEntity<>(showtimes,headers, HttpStatus.OK);
    }

    @RequestMapping(path= "/theater/showtimes/{id}",
            method= RequestMethod.GET,
            produces = { "application/json", "application/xml" })
    public ResponseEntity<Showtime> getShowtime(@RequestHeader(value = "Content-Type", required = false) String contentType,
                                                @PathVariable Long id){
        HttpHeaders headers = validator.getHeaders(contentType);
        Showtime showtime = theaterService.getShowtime(id);
        return new ResponseEntity<>(showtime,headers, HttpStatus.OK);
    }

    @RequestMapping(path= "/theater/showtimes/{id}/bookings",
            method= RequestMethod.POST)
    public ResponseEntity addBooking(@PathVariable Long id,
                                     @Valid @RequestBody Booking booking, Errors errors){
        validator.validateBody(errors);
        theaterService.addBooking(id, booking);
        ResponseEntity<Showtime> response = ResponseEntity.status(HttpStatus.CREATED).build();
        return response;
    }

    @RequestMapping(path= "/theater/showtimes/{id}/bookings",
            method= RequestMethod.PUT,
            produces = { "application/json", "application/xml" })
    public ResponseEntity<Booking> updateBooking(@RequestHeader(value = "Content-Type", required = false) String contentType,
                                                 @PathVariable Long id,
                                                 @Valid @RequestBody Booking booking, Errors errors){
        validator.validateBody(errors);
        HttpHeaders headers = validator.getHeaders(contentType);
        Booking update = theaterService.updateBooking(id, booking);
        return new ResponseEntity<>(update,headers, HttpStatus.OK);
    }

    @RequestMapping(path= "/theater/showtimes/{id}/bookings",
            method= RequestMethod.DELETE)
    public ResponseEntity deleteBooking(@PathVariable Long id,
                                        @RequestBody Booking booking){
        if (booking.userId()==null || booking.userId().isEmpty()){
            throw new BadRequestException("Missing userId in request body");
        }
        theaterService.deleteBooking(id, booking);
        ResponseEntity<Showtime> response = ResponseEntity.status(HttpStatus.OK).build();
        return response;
    }

}