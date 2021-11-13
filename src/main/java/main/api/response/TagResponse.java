package main.api.response;

import main.api.bean.TagBean;

import java.util.List;

public class TagResponse {
    private List<TagBean> tags;

    public List<TagBean> getTags() {
        return tags;
    }

    public void setTags(List<TagBean> tags) {
        this.tags = tags;
    }

}
