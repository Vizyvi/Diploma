package main.controller;

import main.api.response.*;
import main.service.PostsService;
import main.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    @Autowired private InitResponse initResponse;
    @Autowired private SettingsService settingsService;
    @Autowired private PostsService postsService;


    @GetMapping("/init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("/settings")
    private SettingsResponse settings() {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/auth/check")
    private @ResponseBody
    AuthCheckResponse authCheckResponse() {
        AuthCheckResponse bean;
        //TODO : Authentication
        bean = new AuthCheckResponse();
        bean.result = false;
        return bean;
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
