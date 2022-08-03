package main.controller;

import main.api.request.CommentRequest;
import main.api.request.MyProfileChangeRequest;
import main.api.request.PostModerationRequest;
import main.api.response.*;
import main.service.CommentService;
import main.service.PostService;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Calendar;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final PostService postService;
    private final TagService tagService;
    private final CommentService commentService;

    @Autowired
    public ApiGeneralController(InitResponse initResponse, SettingsService settingsService, PostService postService, TagService tagService, CommentService commentService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.postService = postService;
        this.tagService = tagService;
        this.commentService = commentService;
    }


    @GetMapping("/init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("/settings")
    private SettingsResponse settings() {
        return settingsService.getGlobalSettings();
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

    @GetMapping(value = "/calendar")
    public CalendarResponse searchResponse(@RequestParam(required = false) String year) {
        int intYear = year == null ? Calendar.getInstance().get(Calendar.YEAR) : Integer.parseInt(year);
        return postService.getCalendarResponse(intYear);
    }

    @PostMapping(value = "/image")
    public String saveImage(@RequestPart MultipartFile image) throws IOException {
        return settingsService.saveImage(image);
    }

    @PostMapping("/comment")
    public Long addComment(@RequestBody CommentRequest request) throws IOException {
        return commentService.addComment(request);
    }

    @PostMapping("/moderation")
    public Response makeDecision(@RequestBody PostModerationRequest request) {
        return postService.changePostStatus(request);
    }

    @PostMapping(value = "/profile/my", consumes = "multipart/form-data")
    public Response editMyProfile(@RequestPart(required = false) MultipartFile photo,
                                  @RequestParam String email,
                                  @RequestParam String name,
                                  @RequestParam(required = false) String password,
                                  @RequestParam(required = false) Boolean removePhoto) {
        return settingsService.saveProfile(name, email, password, removePhoto, photo);
    }

    @PostMapping(value = "/profile/my", consumes = "application/json")
    public Response editMyProfileWithPhoto(@RequestBody MyProfileChangeRequest request) {
        return settingsService.saveProfile(request.getName(), request.getEmail(), request.getPassword(), request.isRemovePhoto(), null);
    }
}
