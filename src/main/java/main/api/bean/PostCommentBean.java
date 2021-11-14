package main.api.bean;

public class PostCommentBean {
    private Long id;
    private Long timestamp;
    private String text;
    private UserPostCommentBean user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserPostCommentBean getUser() {
        return user;
    }

    public void setUser(UserPostCommentBean user) {
        this.user = user;
    }
}

