package agh.edu.pl.weedesign.library.services;

import agh.edu.pl.weedesign.library.entities.review.Review;
import agh.edu.pl.weedesign.library.entities.review.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review addNewReview(Review review) {
        return this.reviewRepository.save(review);
    }
}
