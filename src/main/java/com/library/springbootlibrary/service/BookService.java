package com.library.springbootlibrary.service;

import com.library.springbootlibrary.dao.BookRepository;
import com.library.springbootlibrary.dao.CheckoutRepository;
import com.library.springbootlibrary.dao.HistoryRepository;
import com.library.springbootlibrary.entity.Book;
import com.library.springbootlibrary.entity.Checkout;
import com.library.springbootlibrary.entity.History;
import com.library.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CheckoutRepository checkoutRepository;
    private final HistoryRepository historyRepository;


    public Book checkoutBook(String userEmail, Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (!book.isPresent() || validateCheckout != null || book.get().getCopiesAvailable() == 0)
            throw new Exception("Book doesnt exist or already checked out by user");

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        bookRepository.save(book.get());

        Checkout checkout = Checkout.builder()
                .userEmail(userEmail)
                .checkoutDate(LocalDate.now().toString())
                .returnDate(LocalDate.now().plusDays(7).toString())
                .bookId(book.get().getId())
                .build();

        checkoutRepository.save(checkout);

        return book.get();
    }

    public Boolean checkoutBookByUser(String userEmail, Long bookId){
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        return validateCheckout != null;
    }

    public int currentLoansCount(String userEmail){
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {
        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();

        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);

        List<Long> bookIdList = new ArrayList<>();

        for (Checkout checkout : checkoutList) {
            bookIdList.add(checkout.getBookId());
        }

        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Book book : books) {
            Optional<Checkout> checkout = checkoutList.stream()
                    .filter(x -> x.getBookId() == book.getId())
                    .findFirst();

            if (checkout.isPresent()) {
                Date date1 = sdf.parse(checkout.get().getReturnDate());
                Date date2 = sdf.parse(LocalDate.now().toString());

                TimeUnit time = TimeUnit.DAYS;

                long difference_In_Time = time.convert(date1.getTime() - date2.getTime(), TimeUnit.MILLISECONDS);

                shelfCurrentLoansResponses.add(ShelfCurrentLoansResponse.builder()
                        .book(book)
                        .daysLeft((int) difference_In_Time)
                        .build());
            }
        }
        return shelfCurrentLoansResponses;
    }

    public void returnBook(String userEmail, Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        Checkout checkout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (!book.isPresent() && checkout == null)
            throw new Exception("Book does not exist or not checked out by user");

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);

        bookRepository.save(book.get());
        checkoutRepository.deleteById(checkout.getId());

        History history = History.builder()
                .userEmail(userEmail)
                .checkoutDate(checkout.getCheckoutDate())
                .returnedDate(LocalDate.now().toString())
                .title(book.get().getTitle())
                .author(book.get().getAuthor())
                .description(book.get().getDescription())
                .img(book.get().getImg())
                .build();

        historyRepository.save(history);
    }

    public void renewLoan(String userEmail, Long bookId) throws Exception {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (validateCheckout == null)
            throw new Exception("Book does not exist or not checked out by user");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = sdf.parse(validateCheckout.getReturnDate());
        Date date2 = sdf.parse(LocalDate.now().toString());

        if (date1.compareTo(date2) > 0 || date1.compareTo(date2) == 0){
            validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepository.save(validateCheckout);
        }
    }
}
