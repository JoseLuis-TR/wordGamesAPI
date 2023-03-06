package com.api.wordgames.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonResponse<T> {

    private HttpStatus status;
    private String message;
    private T responseData;

    public void myResponseError(HttpStatus status, String message, T responseData) {
        Integer statusCode = status.value();
        String statusMsg = status.getReasonPhrase();
        this.message = message;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String date = now.format(formatter);
        this.responseData = responseData;
    }
}
