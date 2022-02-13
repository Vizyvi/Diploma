package main.api.response;

import main.api.bean.PostBean;

import java.util.List;

public class PostResponse {

//    public PostResponse (PostRequest postRequest){}

    private long count;

    private List<PostBean> posts;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<PostBean> getPosts() {
        return posts;
    }

    public void setPosts(List<PostBean> posts) {
        this.posts = posts;
    }
}

