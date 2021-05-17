package main.controller;

import main.api.request.PostRequest;
import main.api.response.*;
import main.model.Post;
import main.service.PostsService;
import main.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final AuthCheckResponse authCheckResponse;
    private final PostsService postsService;

    public ApiGeneralController(InitResponse initResponse, SettingsService service, AuthCheckResponse authCheckResponse, PostsService postsService) {
        this.initResponse = initResponse;
        this.settingsService = service;
        this.authCheckResponse = authCheckResponse;
        this.postsService = postsService;
    }

    @GetMapping("/init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("/settings")
    private SettingsResponse settings() {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/auth/check")
    private AuthCheckResponse authCheckResponse() {
        authCheckResponse.setResult(false);
        return authCheckResponse;
    }

    @GetMapping("/post")
    private PostResponse postResponse(@RequestParam(value = "offset", required = false) Integer offset,
                                      @RequestParam(value = "limit", required = false) Integer limit,
                                      @RequestParam(value = "mode", required = false) String mode) {

        return postsService.getPostResponse();
    }

    @GetMapping("/tag")
    private TagResponse tagResponse() {
        return null;
    }
}
