package main.controller;

import main.api.request.*;
import main.service.SettingsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingsService service;

    public ApiGeneralController(InitResponse initResponse, SettingsService service) {
        this.initResponse = initResponse;
        this.service = service;
    }

    @GetMapping("/init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("/settings")
    private SettingsResponse settings() {
        return service.getGlobalSettings();
    }

    @GetMapping("/auth/check")
    private AuthCheckResponse authCheckResponse() {
        return null;
    }

    @GetMapping("/post")
    private PostResponse postResponse() {
        return null;
    }

    @GetMapping("/tag")
    private TagResponse tagResponse() {
        return null;
    }
}
