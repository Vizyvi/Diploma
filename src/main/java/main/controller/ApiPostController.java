package main.controller;

import main.api.response.PostResponse;
import main.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

@RestController
public class ApiPostController {
    @Autowired private PostsService postService;

    @GetMapping(value = "/api/post/search")
    public @ResponseBody PostResponse getPosts(@RequestParam(required = false) Integer offset,
                          @RequestParam(required = false) Integer limit,
                          @RequestParam(required = false) String query) {
        if(StringUtils.isEmpty(query)) {
//            return "redirect:/init/post";
            return null;
        }
        return null;
    }
}
