package com.library.springbootlibrary.service;

import com.library.springbootlibrary.dao.MessageRepository;
import com.library.springbootlibrary.entity.Message;
import com.library.springbootlibrary.requestmodels.AdminQuestionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public void putMessage(AdminQuestionRequest adminQuestionRequest, String userEmail) throws Exception {
        Optional<Message> message = messageRepository.findById(adminQuestionRequest.getId());

        if (!message.isPresent())
            throw new Exception("Message not found");

        message.get().setAdminEmail(userEmail);
        message.get().setResponse(adminQuestionRequest.getResponse());
        message.get().setClosed(true);
        messageRepository.save(message.get());
    }
}
