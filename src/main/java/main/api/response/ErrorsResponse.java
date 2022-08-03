package main.api.response;

import java.util.HashMap;
import java.util.Map;

public class ErrorsResponse extends Response {
    public Map<String, String> errors = new HashMap<>();
}
