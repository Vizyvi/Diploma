package main.api.response;

import java.util.HashMap;
import java.util.Map;

public class AuthCheckResponseFalse extends AuthCheckResponse{
    public Map<String, String> errors = new HashMap<>();
}
