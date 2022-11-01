package org.booking.system.Service;

import lombok.RequiredArgsConstructor;
import org.booking.system.DTO.MovieDTO.Movie;
import org.booking.system.DTO.ShowtimeDTO.Showtime;
import org.booking.system.Exception.BadRequestException;
import org.booking.system.Exception.NotFoundException;
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
            throw new NotFoundException(String.format("Invalid id: %s", id));
        }

    }

    @Override
    public void createShowtime(Showtime showtime) {
        String imdbID = showtime.getImdbid();
        Movie movie = getMovie(imdbID, "");
        if (movie != null){
            showtime.setTitle(movie.getTitle());
            showtime.setGenre(movie.getGenre());
            showtime.setRuntime(movie.getRuntime());
            showtime.setPlot(movie.getPlot());
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
            throw new NotFoundException(String.format("Invalid id: %s", id));
        }
    }

}
