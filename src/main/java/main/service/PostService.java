package main.service;

import main.api.bean.*;
import main.api.request.AddingPostRequest;
import main.api.request.PostModerationRequest;
import main.api.response.CalendarResponse;
import main.api.response.ErrorsResponse;
import main.api.response.PostResponse;
import main.api.response.Response;
import main.entity.*;
import main.entity.enums.ModerationStatus;
import main.entity.enums.Role;
import main.repository.PostRepository;
import main.repository.TagToPostRepository;
import main.repository.TagsRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final TagsRepository tagRepository;
    private final TagToPostRepository tagToPostRepository;

    public PostService(PostRepository postRepository, UserService userService, TagsRepository tagRepository, TagToPostRepository tagToPostRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.tagRepository = tagRepository;
        this.tagToPostRepository = tagToPostRepository;
    }

    public PostResponse getPostResponse(Integer offset, Integer limit, String mode) {
        int pageNumber = (limit <= 0 || offset < 0) ? 0 : offset / limit;
        Pageable page = PageRequest.of(pageNumber, limit);
        Page<Post> posts;
        switch (mode) {
            case "recent" :
                posts = postRepository.findAllRecent(page);
                break;
            case "early":
                posts = postRepository.findAllEarly(page);
                break;
            case "popular":
                posts = postRepository.findAllPopular(page);
                break;
            case "best":
                posts = postRepository.findAllBest(page);
                break;
            default: posts = Page.empty();
        }

        return getPostResponse(posts);
    }

    public PostResponse getPostBySearch(Integer offset, Integer limit, String query) {
        PostResponse response = new PostResponse();
        List<Post> allPosts;
        if(query == null) {
            allPosts = postRepository.getAllPosts(offset, limit);
        } else {
            query = "%" + query + "%";
            allPosts = postRepository.getPostsByQuery(query, offset, limit);
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
        List<Post> allPosts;
        if(date == null) {
            allPosts = postRepository.getAllPosts(offset, limit);
        } else {
            allPosts = postRepository.getPostsByDate(date, offset, limit);
        }
        return getPostResponse(allPosts);
    }

    public PostResponse getPostByTag(Integer offset, Integer limit, String tag) {
        List<Post> allPosts;
        if(tag == null) {
            allPosts = postRepository.getAllPosts(offset, limit);
        } else {
            tag = "%" + tag + "%";
            allPosts = postRepository.getPostsByTag(tag, offset, limit);
        }
        return getPostResponse(allPosts);
    }

    public UniquePostBean getUniqPostBean(Long id) {
        Post post = postRepository.getOne(id);
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
        String text = post.getText();
        text = removeHTMLAttributes(text);
        if (text.length() > 150) {
            bean.setAnnounce(text.substring(0, 150) + "...");
        } else {
            bean.setAnnounce(text);
        }
        bean.setViewCount(post.getViewCount());
        List<Object[]> info = postRepository.getPostInfo(post.getId());
        bean.setLikeCount(Long.parseLong(info.get(0)[0].toString()));
        bean.setDislikeCount(Long.parseLong(info.get(0)[1].toString()));
        bean.setCommentCount(Long.parseLong(info.get(0)[2].toString()));
    }

    public CalendarResponse getCalendarResponse(int year) {
        CalendarResponse response = new CalendarResponse();
        List<String[]> allPosts = postRepository.findAllActiveByYear(year);
        Map<String, Integer> posts = new LinkedHashMap<>();
        for(String[] row : allPosts) {
            String key = row[0];
            Integer value = Integer.parseInt(row[1]);
            posts.put(key, value);
        }
        response.setPosts(posts);
        response.setYears(postRepository.findAllYearsWithPosts());
        return response;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public PostResponse getAllMyPosts(Integer offset, Integer limit, String status) {
        int pageNumber = (limit <= 0 || offset < 0) ? 0 : offset / limit;
        Pageable page = PageRequest.of(pageNumber, limit);
        Page<Post> posts;
        User user = userService.getCurrentUser();
        if(user == null) {
            return getPostResponse(Page.empty());
        }
        switch (status) {
            case "inactive" :
                posts = postRepository.findMyPostsInactive(user.getId().intValue(), page);
                break;
            case "pending":
                posts = postRepository.findMyPostsPending(user.getId().intValue(), page);
                break;
            case "declined":
                posts = postRepository.findMyPostsDeclined(user.getId().intValue(), page);
                break;
            case "published":
                posts = postRepository.findMyPostsPublished(user.getId().intValue(), page);
                break;
            default: posts = Page.empty();
        }
        return getPostResponse(posts);
    }

    public Response savePost(AddingPostRequest postRequest) {
        ErrorsResponse response = checkPostForErrors(postRequest);
        if (response != null) {
            return response;
        }
        createPost(null, postRequest);
        return Response.success();
    }

    public Response editPost(Long id, AddingPostRequest postRequest){
        Optional<Post> optionalPost = postRepository.findById(id);
        Post post;
        if(optionalPost.isPresent()) {
            post = optionalPost.get();
        } else {
            return new ErrorsResponse();
        }
        ErrorsResponse response = checkPostForErrors(postRequest);
        if (response != null) {
            return response;
        }
        Post savedPost = createPost(post, postRequest);
        User user = userService.getCurrentUser();
        if(user.getRole().equals(Role.MODERATOR)) {
            savedPost.setModerationStatus(post.getModerationStatus());
            postRepository.save(savedPost);
        }
        return Response.success();
    }

    private Post createPost(Post post, AddingPostRequest postRequest) {
        if(post == null) {
            post = new Post();
        }
        long currentTime = System.currentTimeMillis();
        if(currentTime > postRequest.getTimestamp() * 1000) {
            post.setTime(new Date(currentTime));
        } else {
            post.setTime(new Date(postRequest.getTimestamp()));
        }
        post.setActive(postRequest.getActive());
        post.setTitle(removeHTMLAttributes(postRequest.getTitle()));
        post.setText(removeHTMLAttributes(postRequest.getText()));
        post.setModerationStatus(ModerationStatus.NEW);
        post.setUser(userService.getCurrentUser());
        Post savedPost = postRepository.save(post);
        List<Tag> tags = new ArrayList<>();
        for(String str : postRequest.getTags()) {
            Optional<Tag> optionalTag = tagRepository.findByName(str);
            if(optionalTag.isPresent()){
                tags.add(optionalTag.get());
            } else {
                Tag tag = new Tag();
                tag.setName(str);
                tags.add(tagRepository.save(tag));
            }
        }
        List<TagToPost> tagToPosts = tags.stream()
                .map(tag -> new TagToPost(savedPost.getId(), tag.getId()))
                .collect(Collectors.toList());
        tagToPostRepository.saveAll(tagToPosts);
        return savedPost;
    }

    private ErrorsResponse checkPostForErrors(AddingPostRequest postRequest) {
        ErrorsResponse response = new ErrorsResponse();
        String title = postRequest.getTitle();
        if(!StringUtils.hasText(title)) {
            response.errors.put("title", "Заголовок не может быть пустым");
        }
        if(title.length() < 3) {
            response.errors.put("title", "Заголовок слишком короткий (3 символа мин)");
        }
        String text = postRequest.getText();
        if(!StringUtils.hasText(text)) {
            response.errors.put("text", "Текст публикации не может быть пустым");
        }
        if(text.length() < 50) {
            response.errors.put("text", "Текст публикации слишком короткий (50 символов мин)");
        }
        if(!response.errors.isEmpty()) {
            return response;
        }
        return null;
    }

    private PostResponse getPostResponse(List<Post> allPosts) {
        PostResponse response = new PostResponse();
        response.setCount(allPosts.size());
        List<PostBean> list = allPosts.stream().map(p -> {
            PostBean bean = new PostBean();
            copyDataToBeanFromEntity(bean, p);
            return bean;
        }).collect(Collectors.toList());
        response.setPosts(list);
        return response;
    }

    private PostResponse getPostResponse(Page<Post> allPosts) {
        PostResponse response = new PostResponse();
        response.setCount(allPosts.getTotalElements());
        List<PostBean> list = allPosts.stream().map(p -> {
            PostBean bean = new PostBean();
            copyDataToBeanFromEntity(bean, p);
            return bean;
        }).collect(Collectors.toList());
        response.setPosts(list);
        return response;
    }

    public String removeHTMLAttributes(String text) {
        return Jsoup.clean(text, Safelist.none());
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Response changePostStatus(PostModerationRequest request) {
        User currentUser = userService.getCurrentUser();
        if(currentUser == null || !currentUser.isModerator()) {
            return new Response();
        }
        Post post = postRepository.getOne(request.getPostId());
        if(post == null || !post.getModerationStatus().equals(ModerationStatus.NEW)) {
            return new Response();
        }
        post.setModeratorId(currentUser.getId().intValue());
        ModerationStatus status = request.getDecision().equals("accept") ? ModerationStatus.ACCEPTED : ModerationStatus.DECLINED;
        post.setModerationStatus(status);
        postRepository.save(post);
        return Response.success();
    }
}
