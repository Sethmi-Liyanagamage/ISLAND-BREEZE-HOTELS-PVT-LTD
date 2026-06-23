package com.hotelmanagementsystem.controller;

import com.hotelmanagementsystem.model.Review;
import com.hotelmanagementsystem.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        try {
            Review createdReview = reviewService.createReview(review);
            return ResponseEntity.ok(createdReview);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<Review>> getReviewsByBooking(@PathVariable String bookingId) {
        return ResponseEntity.ok(reviewService.getReviewsByBookingId(bookingId));
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<Review>> getReviewsByGuest(@PathVariable String guestId) {
        return ResponseEntity.ok(reviewService.getReviewsByGuestId(guestId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable String reviewId, @RequestBody Review review) {
        try {
            Review updatedReview = reviewService.updateReview(reviewId, review);
            return ResponseEntity.ok(updatedReview);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String reviewId) {
        boolean deleted = reviewService.deleteReview(reviewId);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/approved")
    public ResponseEntity<List<Review>> getApprovedReviews() {
        return ResponseEntity.ok(reviewService.getApprovedReviews());
    }
    
    @GetMapping("/rating/{minRating}")
    public ResponseEntity<List<Review>> getReviewsByRating(@PathVariable int minRating) {
        return ResponseEntity.ok(reviewService.getReviewsByRating(minRating));
    }
}