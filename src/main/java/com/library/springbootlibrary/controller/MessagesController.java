package com.library.springbootlibrary.controller;

import com.library.springbootlibrary.entity.Message;
import com.library.springbootlibrary.requestmodels.AdminQuestionRequest;
import com.library.springbootlibrary.service.MessagesService;
import com.library.springbootlibrary.utils.ExtractJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessagesController {
    private final MessagesService messagesService;

    @PostMapping("/secure/add/message")
    public void postMessage(@RequestHeader(value = "Authorization") String token,
                            @RequestBody Message messageRequest) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        messagesService.postMessage(messageRequest, userEmail);
    }

    @PutMapping("/secure/admin/message")
    public void putMessage(@RequestHeader(value = "Authorization") String token,
                           @RequestBody AdminQuestionRequest adminQuestionRequest) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");

        if (admin == null || "admin".equals(admin))
            throw new Exception("Administraton page only");

        messagesService.putMessage(adminQuestionRequest, userEmail);
    }
}
