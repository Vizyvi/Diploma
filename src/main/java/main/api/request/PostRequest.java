package main.api.request;

import org.springframework.stereotype.Component;

@Component
public class PostRequest {

    private Integer offset;
    private Integer limit;
    private String mode;

    public PostRequest(Integer offset, Integer limit, String mode) {
        this.limit = limit;
        this.offset = offset;
        this.mode = mode;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
