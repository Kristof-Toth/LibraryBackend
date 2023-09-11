package com.library.springbootlibrary.responsemodels;

import com.library.springbootlibrary.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShelfCurrentLoansResponse {

    private Book book;

    private int daysLeft;
}
