package com.library.springbootlibrary.service;

import com.library.springbootlibrary.dao.BookRepository;
import com.library.springbootlibrary.dao.ReviewRepository;
import com.library.springbootlibrary.entity.Review;
import com.library.springbootlibrary.requestmodels.ReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception {
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, reviewRequest.getBookId());

        if (validateReview != null){
            throw new Exception("Review already created");
        }

        Review review = Review.builder()
                .rating(reviewRequest.getRating())
                .bookId(reviewRequest.getBookId())
                .userEmail(userEmail)
                .reviewDescription(reviewRequest.getReviewDescription().map(Objects::toString).orElse(null))
                .date(Date.valueOf(LocalDate.now()))
                .build();
        // ha a .reviewDescription nem működik
        /* if (reviewRequest.getReviewDescription().isPresent()) {
            review.setReviewDescription(reviewRequest.getReviewDescription()
                    .map(Objects::toString).orElse(null));
        } */

        reviewRepository.save(review);
    }

    public Boolean userReviewListed(String userEmail, Long bookId) {
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, bookId);

        return validateReview != null;
    }
}
