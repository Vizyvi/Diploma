package main.api.bean;

import java.util.List;

public class UniquePostBean extends PostBean{

    private List<PostCommentBean> comments;
    private List<String> tags;

    public List<PostCommentBean> getComments() {
        return comments;
    }

    public void setComments(List<PostCommentBean> comments) {
        this.comments = comments;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
