package main.service;

import main.api.bean.PostBean;
import main.api.bean.UserPostBean;
import main.api.response.PostResponse;
import main.entity.ModerationStatus;
import main.entity.Post;
import main.entity.Tag;
import main.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
        List<PostBean> list = allPosts.stream().map(this::createBeanFromEntity).collect(Collectors.toList());
        response.setPosts(list);
        return response;
    }

    public PostBean createBeanFromEntity(Post post) {
        PostBean bean = new PostBean();
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
        return bean;
    }

    public List<Post> getAllPosts() {
        return postsRepository.findAll();
    }
}
