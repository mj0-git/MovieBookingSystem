package org.booking.system.Service;

import lombok.RequiredArgsConstructor;
import org.booking.system.DTO.Account;
import org.booking.system.DTO.MovieDTO.Movie;
import org.booking.system.DTO.ShowtimeDTO.Booking;
import org.booking.system.DTO.ShowtimeDTO.Showtime;
import org.booking.system.Exception.BadRequestException;
import org.booking.system.Exception.NotFoundException;
import org.booking.system.Repo.BookingRepository;
import org.booking.system.Repo.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TheaterServiceImpl implements TheaterService {

    @Autowired
    private ShowtimeRepository showtimeRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AccountService accountService;
    @Autowired
    private final String omdbUrl;
    @Autowired
    private final RestTemplate restTemplate;

    @Override
    public Movie getMovie(String id, String title) {
        Movie resp = null;
        if (null!=id && !id.isEmpty()){
            resp = restTemplate.getForObject(omdbUrl, Movie.class, id, "");
        }
        else{
            resp = restTemplate.getForObject(omdbUrl, Movie.class,"", title);
        }
        if (resp.getTitle() == null){
            throw new NotFoundException(String.format("Movie not found with: id=%s, title=%s", id, title));
        }
        return resp;
    }

    @Override
    public List<Showtime> getAllShowtimes() {
        return (List<Showtime>) showtimeRepository.findAll();
    }

    @Override
    public Showtime getShowtime(Long id) {
        Optional<Showtime> showtime =  showtimeRepository.findById(id);
        if (showtime.isPresent()){
            return showtime.get();
        }else{
            throw new NotFoundException(String.format("Invalid showtime id: %s", id));
        }

    }

    @Override
    public void createShowtime(Showtime showtime) {
        String imdbID = showtime.getImdbid();
        Movie movie = getMovie(imdbID, "");
        if (movie != null){
            showtime.setTitle(movie.getTitle());
            showtime.setRated(movie.getRated());
            showtime.setPlot(movie.getPlot());
            showtime.setRuntime(movie.getRuntime());
            showtimeRepository.save(showtime);
        }
        else {
            throw new BadRequestException("Invalid showtime object properties");
        }
    }

    @Override
    public void deleteShowtime(Long id) {
        Optional<Showtime> showtime =  showtimeRepository.findById(id);
        if(showtime.isPresent()){
            showtimeRepository.deleteById(id);
        }
        else{
            throw new NotFoundException(String.format("Invalid showtime id: %s", id));
        }
    }


    @Override
    public void addBooking(Long id, Booking booking) {
        Optional<Showtime> s =  showtimeRepository.findById(id);
        if(s.isPresent()){
            Showtime showtime = s.get();
            Long userId = booking.getUserId();
            Optional<Account> account =  accountService.fetchAccount(userId);
            if (!account.isPresent()){
                throw new BadRequestException(String.format("Account not found with userId: %s", userId));
            }
            else {
                int quantity = booking.getQuantity();
                int capacity = showtime.getCapacity();
                //Check if quantity < capacity is valid
                if (capacity - quantity >= 0) {
                    capacity = capacity - quantity;
                    showtime.setCapacity(capacity);
                    showtimeRepository.save(showtime);
                    booking.setShowtime(showtime);
                    booking.setAccount(account.get());
                    bookingRepository.save(booking);
                } else {
                    throw new BadRequestException(String.format("Quantity exceeds capacity, please change: %s", quantity));
                }
            }
        }
        else{
            throw new NotFoundException(String.format("Invalid showtime id: %s", id));
        }
    }

    @Override
    public Booking updateBooking(Long id, Booking booking) {
        //
        Long userId = booking.getUserId();
        Optional<Booking> b = bookingRepository.getBookingByUserIdAndShowtimeId(userId, id);
        Optional<Showtime> s =  showtimeRepository.findById(id);
        if(s.isPresent() && b.isPresent()){
            Showtime showtime = s.get();
            Booking repo_booking = b.get();
            int capacity = showtime.getCapacity() + repo_booking.getQuantity();
            int quantity = booking.getQuantity();
            if (capacity - quantity >= 0){
                capacity = capacity - quantity;
                showtime.setCapacity(capacity);
                showtimeRepository.save(showtime);
                repo_booking.setQuantity(quantity);
                bookingRepository.save(repo_booking);
                return  repo_booking;
            }
            else{
                throw new BadRequestException(String.format("Quantity exceeds capacity, please change: %s", quantity));
            }
        }
        else{
            errorMessageBooking(id, userId, b, s);
            return null;
        }
    }

    @Override
    public void deleteBooking(Long id, Booking booking) {
        Long userId = booking.getUserId();
        Optional<Booking> b = bookingRepository.getBookingByUserIdAndShowtimeId(userId, id);
        Optional<Showtime> s =  showtimeRepository.findById(id);
        if(b.isPresent() && s.isPresent()){
            Showtime showtime = s.get();
            Booking repo_booking = b.get();
            int capacity = showtime.getCapacity() + repo_booking.getQuantity();
            showtime.setCapacity(capacity);
            showtimeRepository.save(showtime);
            bookingRepository.delete(b.get());
        }
        else{
            errorMessageBooking(id, userId, b, s);
        }
    }

    private void errorMessageBooking(Long id, Long userId, Optional<Booking> b, Optional<Showtime> s) {
         if(!s.isPresent()){
            throw new NotFoundException(String.format("Invalid showtime id: %s", id));
        }
        else{
            throw new NotFoundException(String.format("Booking not found with userId: %s", userId));
        }
    }

    @Override
    public List<Booking> getBookingByShowtimeId(Long id) {
        Optional<Showtime> showtime =  showtimeRepository.findById(id);
        if(showtime.isPresent()){
            return bookingRepository.getBookingByShowtimeId(id);
        }
        else{
            throw new NotFoundException(String.format("Invalid id: %s", id));
        }
    }

    @Override
    public List<Booking> getBookingByUserId(Long userId) {
        //Optional<Showtime> showtime =  FIND by account
        //if(showtime.isPresent()){
            return bookingRepository.getBookingByUserId(userId);
        //}
        //else{
        //    throw new NotFoundException(String.format("Invalid id: %s", id));
        //}
    }

}
