package org.booking.system;

import org.booking.system.DTO.ShowtimeDTO.Showtime;
import org.booking.system.Service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    TheaterService theaterService;

    public void run(ApplicationArguments args) {
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateObj.format(formatter);
        Showtime showtime1 = new Showtime("tt15474916", date + " 12pm", 20 );
        theaterService.createShowtime(showtime1);
        Showtime showtime2 = new Showtime("tt1457767", date + " 12pm", 20 );
        theaterService.createShowtime(showtime2);
        Showtime showtime3 = new Showtime("tt0077651", date + " 12pm", 20 );
        theaterService.createShowtime(showtime3);
    }
}
