package main.service;

import main.api.bean.*;
import main.api.response.PostResponse;
import main.entity.*;
import main.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostsService {

    @Autowired private PostsRepository postsRepository;

    public PostResponse getPostResponse(Integer offset, Integer limit, String mode) {
        PostResponse response = new PostResponse();
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        List<Post> allPosts;
        switch (mode) {
            case "recent" :
                allPosts = postsRepository.findAllByActiveAndStatusAndTimeRecent(true, ModerationStatus.ACCEPTED.toString(), strDate, offset, limit);
                break;
            case "early":
                allPosts = postsRepository.findAllByActiveAndStatusAndTimeEarly(true, ModerationStatus.ACCEPTED.toString(), strDate, offset, limit);
                break;
            case "popular":
                allPosts = postsRepository.findAllByActiveAndStatusAndTimePopular(true, ModerationStatus.ACCEPTED.toString(), strDate, offset, limit);
                break;
            case "best":
                allPosts = postsRepository.findAllByActiveAndStatusAndTimeBest(true, ModerationStatus.ACCEPTED.toString(), strDate, offset, limit);
                break;
            default: allPosts = new ArrayList<>();
        }
        response.setCount(allPosts.size());
        List<PostBean> list = allPosts.stream().map(p -> {
            PostBean bean = new PostBean();
            copyDataToBeanFromEntity(bean, p);
            return bean;
        }).collect(Collectors.toList());
        response.setPosts(list);
        return response;
    }

    public PostResponse getPostBySearch(Integer offset, Integer limit, String query) {
        PostResponse response = new PostResponse();
        List<Post> allPosts;
        if(query == null) {
            allPosts = postsRepository.getAllPosts(offset, limit);
        } else {
            query = "%" + query + "%";
            allPosts = postsRepository.getPostsByQuery(query, offset, limit);
        }
        response.setCount(allPosts.size());
        List<PostBean> list = allPosts.stream().map(p -> {
            PostBean bean = new PostBean();
            copyDataToBeanFromEntity(bean, p);
            return bean;
        }).collect(Collectors.toList());
        response.setPosts(list);
        return response;
    }

    public PostResponse getPostByDate(Integer offset, Integer limit, String date) {
        PostResponse response = new PostResponse();
        List<Post> allPosts;
        if(date == null) {
            allPosts = postsRepository.getAllPosts(offset, limit);
        } else {
            allPosts = postsRepository.getPostsByDate(date, offset, limit);
        }
        response.setCount(allPosts.size());
        List<PostBean> list = allPosts.stream().map(p -> {
            PostBean bean = new PostBean();
            copyDataToBeanFromEntity(bean, p);
            return bean;
        }).collect(Collectors.toList());
        response.setPosts(list);
        return response;
    }

    public PostResponse getPostByTag(Integer offset, Integer limit, String tag) {
        PostResponse response = new PostResponse();
        List<Post> allPosts;
        if(tag == null) {
            allPosts = postsRepository.getAllPosts(offset, limit);
        } else {
            tag = "%" + tag + "%";
            allPosts = postsRepository.getPostsByTag(tag, offset, limit);
        }
        response.setCount(allPosts.size());
        List<PostBean> list = allPosts.stream().map(p -> {
            PostBean bean = new PostBean();
            copyDataToBeanFromEntity(bean, p);
            return bean;
        }).collect(Collectors.toList());
        response.setPosts(list);
        return response;
    }

    public UniquePostBean getUniqPostBean(Long id) {
        Post post = postsRepository.getOne(id);
        if (post == null) {
            return null;
        }
        UniquePostBean bean = new UniquePostBean();
        copyDataToBeanFromEntity(bean, post);
        List<PostCommentBean> comments = new ArrayList<>();
        List<PostComment> postComments = post.getPostComments();
        for(PostComment comment : postComments) {
            PostCommentBean postCommentBean = new PostCommentBean();
            postCommentBean.setId(comment.getId());
            postCommentBean.setTimestamp(post.getTime().getTime() / 1000);
            postCommentBean.setText(comment.getText());
            User user = post.getUser();
            UserPostCommentBean userBean = new UserPostCommentBean();
            userBean.setId(user.getId());
            userBean.setName(user.getName());
            userBean.setPhoto(user.getPhoto());
            postCommentBean.setUser(userBean);
            comments.add(postCommentBean);
        }
        List<String> tags = post.getTags().stream().map(Tag::getName).collect(Collectors.toList());
        bean.setComments(comments);
        bean.setTags(tags);
        return bean;
    }

    public void copyDataToBeanFromEntity(PostBean bean, Post post) {
        UserPostBean userPostBean = new UserPostBean();
        userPostBean.setId(post.getUser().getId());
        userPostBean.setName(post.getUser().getName());

        bean.setId(post.getId());
        bean.setTimestamp(post.getTime().getTime() / 1000);
        bean.setUser(userPostBean);
        bean.setTitle(post.getTitle());
        if (post.getText().length() > 150) {
            bean.setAnnounce(post.getText().substring(0, 150) + "...");
        } else {
            bean.setAnnounce(post.getText());
        }
        bean.setViewCount(post.getViewCount());
        List<Object[]> info = postsRepository.getPostInfo(post.getId());
        bean.setLikeCount(Long.parseLong(info.get(0)[0].toString()));
        bean.setDislikeCount(Long.parseLong(info.get(0)[1].toString()));
        bean.setCommentCount(Long.parseLong(info.get(0)[2].toString()));
    }

    public List<Post> getAllPosts() {
        return postsRepository.findAll();
    }
}
