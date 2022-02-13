package main.api.response;

import java.util.HashMap;
import java.util.Map;

public class ApiAuthResponseFalse extends Response {
    public Map<String, String> errors = new HashMap<>();
}
