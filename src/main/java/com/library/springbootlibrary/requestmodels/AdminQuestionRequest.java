package com.library.springbootlibrary.requestmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminQuestionRequest {
    private Long id;

    private String response;
}
