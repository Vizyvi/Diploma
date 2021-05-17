package main.service;

import main.api.request.PostRequest;
import main.api.response.PostResponse;
import main.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

@Service
public class PostsService {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private PostRequest postRequest;
    @Autowired
    private PostResponse postResponse;

    public void setCountOfPosts() {
        postResponse.setCount((int) StreamSupport.stream(postsRepository.findAll().spliterator(), false).count());
    }

    public PostResponse getPostResponse() {
        setCountOfPosts();
        return postResponse;
    }
}
