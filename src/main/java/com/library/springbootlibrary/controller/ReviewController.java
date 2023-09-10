package com.library.springbootlibrary.controller;

import com.library.springbootlibrary.requestmodels.ReviewRequest;
import com.library.springbootlibrary.service.ReviewService;
import com.library.springbootlibrary.utils.ExtractJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/secure/user/book")
    public Boolean reviewBookByUser(@RequestHeader(value = "Authorization") String token,
                                    @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");

        if (userEmail == null)
            throw new Exception("User email is missing");

        return reviewService.userReviewListed(userEmail, bookId);
    }

    @PostMapping("/secure")
    public void postReview(@RequestHeader(value = "Authorization") String token,
                           @RequestBody ReviewRequest reviewRequest) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");

        if (userEmail == null)
            throw new Exception("User email is missing");


        reviewService.postReview(userEmail, reviewRequest);
    }
}
