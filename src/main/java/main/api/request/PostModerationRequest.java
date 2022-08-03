package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostModerationRequest {

    @JsonProperty("post_id")
    private Long postId;
    private String decision;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
