package org.booking.system.Service;

import lombok.RequiredArgsConstructor;
import org.booking.system.DTO.MovieDTO.Movie;
import org.booking.system.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TheaterServiceImpl implements TheaterService {

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
}
