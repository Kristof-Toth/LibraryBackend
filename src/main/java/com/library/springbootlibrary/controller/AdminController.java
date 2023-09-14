package com.library.springbootlibrary.controller;

import com.library.springbootlibrary.requestmodels.AddBookRequest;
import com.library.springbootlibrary.service.AdminService;
import com.library.springbootlibrary.utils.ExtractJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/secure/add/book")
    public void postBook(@RequestHeader(value = "Authorization") String token,
                         @RequestBody AddBookRequest addBookRequest) throws Exception {
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");

        if ("admin".equals(admin))
            throw new Exception("Admin only page");

        adminService.postBook(addBookRequest);
    }
}
