package com.library.springbootlibrary.service;

import com.library.springbootlibrary.dao.MessageRepository;
import com.library.springbootlibrary.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MessagesService {
    private final MessageRepository messageRepository;

    public void postMessage(Message messageRequest, String userEmail) {
        Message message = Message.builder()
                .userEmail(userEmail)
                .title(messageRequest.getTitle())
                .question(messageRequest.getQuestion())
                .build();

        messageRepository.save(message);
    }
}
