package main.exception;

import main.api.response.ErrorsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DownloadFileExceptionHandler {

    @ExceptionHandler(value = {DownloadFileException.class})
    public ResponseEntity<Object> handleException(DownloadFileException ex) {
        ErrorsResponse response = new ErrorsResponse();
        response.errors.put("image", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
