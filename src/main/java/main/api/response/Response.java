package main.api.response;

public class Response {
    public Response() {}

    private Response(boolean result) {
        this.result = result;
    }

    public static Response success() {
        return new Response(true);
    }

    public boolean result = false;
}
