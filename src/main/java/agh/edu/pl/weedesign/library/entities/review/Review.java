package agh.edu.pl.weedesign.library.entities.review;

import org.springframework.cglib.core.Local;

import agh.edu.pl.weedesign.library.entities.rental.Rental;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="review")
public class Review {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    private int stars;

    private String comment;

    private LocalDateTime dateTime;
    @OneToOne(mappedBy = "review")
    private Rental rental;


    public Review(){};

    public Review(int stars, LocalDateTime dateTime){
        this.stars = stars;
        this.comment = "";
        this.dateTime = dateTime;
    }

    public Review(int stars, String comment, LocalDateTime dateTime){
        this.stars = stars;
        this.comment = comment;
        this.dateTime = dateTime;
    }
    public Review(int stars, String comment, LocalDateTime dateTime, Rental rental){
        this.stars = stars;
        this.comment = comment;
        this.dateTime = dateTime;
        this.rental = rental;
    }


    public int getId() {
        return id;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
