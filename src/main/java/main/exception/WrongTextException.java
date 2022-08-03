package main.exception;

public class WrongTextException extends RuntimeException {

    public WrongTextException(String error) {
        super(error);
    }

}
