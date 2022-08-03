package main.service;

import main.api.bean.TagBean;
import main.api.response.TagResponse;
import main.entity.Post;
import main.entity.Tag;
import main.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagsRepository tagsRepository;
    private final PostService postService;

    @Autowired
    public TagService(PostService postService, TagsRepository tagsRepository) {
        this.postService = postService;
        this.tagsRepository = tagsRepository;
    }

    public TagResponse getTagResponse(String query) {
        TagResponse tagResponse = new TagResponse();
        List<Tag> all;
        if(query == null) {
            all = tagsRepository.findAll();
        } else {
            query = "%"+query+"%";
            all = tagsRepository.getAllByQuery(query);
        }
        List<String> tagNames = all.stream().map(Tag::getName).collect(Collectors.toList());
        List<TagBean> tagBeans = getTagBeans(tagNames);
        tagResponse.setTags(tagBeans);
        return tagResponse;
    }

    private List<TagBean> getTagBeans(List<String> tagNames) {
        List<TagBean> tagBeans = new ArrayList<>();
        Integer max = tagsRepository.getCountMostPopularTag();
        List<Post> list = postService.getAllPosts();
        long allQuantity = list.size();
        List<Object[]> counts = tagsRepository.getCountForListName(tagNames);
        for(Object[] obj : counts) {
            String tagName = obj[0].toString();
            int count = Integer.parseInt(obj[1].toString());
            double k =  1 / ((double)max / allQuantity);
            double weight =  k * count / allQuantity ;
            TagBean bean = new TagBean(tagName, weight);
            tagBeans.add(bean);
        }
        return tagBeans;
    }


}
