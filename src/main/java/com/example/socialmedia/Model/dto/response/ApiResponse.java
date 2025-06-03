package com.example.socialmedia.Model.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse {
    private String message;
    private int code;
    private HttpStatus httpStatus;
}
