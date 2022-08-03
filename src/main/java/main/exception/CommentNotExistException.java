package main.exception;

public class CommentNotExistException extends RuntimeException {

    public CommentNotExistException(String error) {
        super(error);
    }

}
