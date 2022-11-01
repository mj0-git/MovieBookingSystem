package org.booking.system.DTO.ShowtimeDTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("plot")
    private String plot;

    @JsonProperty("genre")
    private String genre;

    @JsonProperty("runtime")
    private String runtime;

    @JsonProperty("time")
    @NotNull
    public String time;

    @JsonProperty("bookingids")
    @ElementCollection
    public List<String> bookingIDs = new ArrayList<>();

    @JsonProperty("capacity")
    @NotNull
    public int capacity;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonAlias("imdb-id")
    private String imdbid;


    public Showtime(String imdbid, String time, int capacity){
        this.imdbid = imdbid;
        this.time = time;
        this.capacity = capacity;
    }

}
