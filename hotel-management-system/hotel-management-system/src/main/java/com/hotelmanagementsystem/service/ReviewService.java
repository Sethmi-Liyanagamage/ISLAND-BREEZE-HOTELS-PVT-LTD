package com.hotelmanagementsystem.service;

import com.hotelmanagementsystem.model.Review;
import com.hotelmanagementsystem.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review createReview(Review review) {
        if (review.getCreatedAt() == null) {
            review.setCreatedAt(LocalDateTime.now());
        }
        if (review.getStatus() == null || review.getStatus().isEmpty()) {
            review.setStatus("PENDING");
        }
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByBookingId(String bookingId) {
        return reviewRepository.findByBookingId(bookingId);
    }

    public List<Review> getReviewsByGuestId(String guestId) {
        return reviewRepository.findByGuestId(guestId);
    }
    
    public Optional<Review> getReviewById(String reviewId) {
        return reviewRepository.findById(reviewId);
    }

    public Review updateReview(String reviewId, Review updatedReview) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new NoSuchElementException("Review not found with id: " + reviewId);
        }
        updatedReview.setReviewId(reviewId);
        return reviewRepository.save(updatedReview);
    }
    
    public boolean deleteReview(String reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            return false;
        }
        reviewRepository.deleteById(reviewId);
        return true;
    }

    public List<Review> getApprovedReviews() {
        return reviewRepository.findByStatus("APPROVED");
    }
    
    public List<Review> getReviewsByRating(int minRating) {
        return reviewRepository.findByRatingGreaterThanEqual(minRating);
    }
    
    public double getAverageRatingForBooking(String bookingId) {
        List<Review> reviews = reviewRepository.findByBookingId(bookingId);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
}