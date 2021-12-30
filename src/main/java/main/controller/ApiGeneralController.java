package main.controller;

import main.api.response.*;
import main.service.PostsService;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final PostsService postService;
    private final TagService tagService;

    @Autowired
    public ApiGeneralController(InitResponse initResponse, SettingsService settingsService, PostsService postService, TagService tagService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.postService = postService;
        this.tagService = tagService;
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
        offset = offset == null ? 0 : offset;
        limit = limit == null || limit > 10 || limit < 1 ? 10 : limit;
        mode = mode == null ? "recent" : mode;
        return postService.getPostResponse(offset, limit, mode);
    }

    @GetMapping("/tag")
    private TagResponse tagResponse(@RequestParam(value ="query", required = false) String query) {
        return tagService.getTagResponse(query);
    }

//    @GetMapping(value = "/calendar")
//    public String searchResponse(@RequestParam(required = false) String year) {
//
//    }
}
