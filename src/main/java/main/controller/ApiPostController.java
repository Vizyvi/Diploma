package main.controller;

import main.api.bean.UniquePostBean;
import main.api.response.PostResponse;
import main.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {
    @Autowired private PostsService postService;

    @GetMapping(value = "/search")
    public PostResponse getPostsBySearch(@RequestParam(required = false) Integer offset,
                          @RequestParam(required = false) Integer limit,
                          @RequestParam(required = false) String query) {
        offset = offset == null ? 0 : offset;
        limit = limit == null || limit > 10 || limit < 1 ? 10 : limit;
        return postService.getPostBySearch(offset, limit, query);
    }

    @GetMapping(value = "/byDate")
    public PostResponse getPostsByDate(@RequestParam(required = false) Integer offset,
                                 @RequestParam(required = false) Integer limit,
                                 @RequestParam(required = false) String date) {
        offset = offset == null ? 0 : offset;
        limit = limit == null || limit > 10 || limit < 1 ? 10 : limit;
        return postService.getPostByDate(offset, limit, date);
    }

    @GetMapping(value = "/byTag")
    public PostResponse getPostsByTag(@RequestParam(required = false) Integer offset,
                                       @RequestParam(required = false) Integer limit,
                                       @RequestParam(required = false) String tag) {
        offset = offset == null ? 0 : offset;
        limit = limit == null || limit > 10 || limit < 1 ? 10 : limit;
        return postService.getPostByTag(offset, limit, tag);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getPostById(@PathVariable Long id) {
        UniquePostBean response = postService.getUniqPostBean(id);
        if(response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity(response, HttpStatus.OK);

    }

    @GetMapping("/my")
    public ResponseEntity<PostResponse> getAllMyPosts(@RequestParam(required = false) Integer offset,
                                                      @RequestParam(required = false) Integer limit,
                                                      @RequestParam(required = false) String status) {
        offset = offset == null ? 0 : offset;
        limit = limit == null || limit > 10 || limit < 1 ? 10 : limit;
        status = status == null ? "published" : status;
        PostResponse response = postService.getAllMyPosts(offset, limit, status);
        return ResponseEntity.ok(response);
    }
}
