package main.exception;

public class PostNotExistException extends RuntimeException {

    public PostNotExistException(String error) {
        super(error);
    }

}
