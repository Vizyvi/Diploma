package main.service;

import main.api.request.CommentRequest;
import main.entity.Post;
import main.entity.PostComment;
import main.exception.CommentNotExistException;
import main.exception.PostNotExistException;
import main.exception.WrongTextException;
import main.repository.PostCommentRepository;
import main.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class CommentService {
    private final static int COMMENT_LENGTH = 10;

    private final PostService postService;
    private final PostCommentRepository postCommentRepository;

    public CommentService(PostService postService, PostCommentRepository postCommentRepository) {
        this.postService = postService;
        this.postCommentRepository = postCommentRepository;
    }

    public Long addComment(CommentRequest request) {
        Long parentId = request.getParentId();
        if(parentId != null) {
            postCommentRepository.findById(parentId).orElseThrow(() -> new CommentNotExistException("Родительский комментарий не был найден"));
        }

        Long postId = request.getPostId();
        postService.getPostById(postId).orElseThrow(() -> new PostNotExistException("Пост для этого коментария не был найден"));

        String text = postService.removeHTMLAttributes(request.getText());
        if(StringUtils.hasText(text) || text.length() < COMMENT_LENGTH) {
            throw new WrongTextException("Комментарий пуст или слишком короткий (меньше " + COMMENT_LENGTH + " символов)");
        }

        long currentTime = System.currentTimeMillis();
        PostComment postComment = new PostComment();
        postComment.setPostId(postId);
        postComment.setText(text);
        postComment.setParentId(parentId);
        postComment.setTime(new Date(currentTime));
        PostComment savedPostComment = postCommentRepository.save(postComment);
        return savedPostComment.getId();
    }

}
