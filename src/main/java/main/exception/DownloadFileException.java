package main.exception;

public class DownloadFileException extends RuntimeException {

    public DownloadFileException(String message) {
        super(message);
    }
}
