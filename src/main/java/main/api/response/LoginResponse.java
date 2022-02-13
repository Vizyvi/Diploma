package main.api.response;

public class LoginResponse extends Response {

    public LoginResponse(UserResponse userResponse) {
        result = true;
        user = userResponse;
    }

    private UserResponse user;

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
