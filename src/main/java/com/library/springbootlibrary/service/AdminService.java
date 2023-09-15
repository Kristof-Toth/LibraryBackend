package com.library.springbootlibrary.service;

import com.library.springbootlibrary.dao.BookRepository;
import com.library.springbootlibrary.entity.Book;
import com.library.springbootlibrary.requestmodels.AddBookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final BookRepository bookRepository;

    public void postBook(AddBookRequest addBookRequest) {
        Book book = Book.builder()
                .title(addBookRequest.getTitle())
                .author(addBookRequest.getAuthor())
                .description(addBookRequest.getDescription())
                .copies(addBookRequest.getCopies())
                .copiesAvailable(addBookRequest.getCopies())
                .category(addBookRequest.getCategory())
                .img(addBookRequest.getImg())
                .build();

        bookRepository.save(book);
    }
}
