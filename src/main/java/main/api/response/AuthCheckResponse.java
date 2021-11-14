package main.api.response;

public class AuthCheckResponse {
    public AuthCheckResponse() {}

    public AuthCheckResponse(boolean result) {
        this.result = result;
    }

    public boolean result;
}
