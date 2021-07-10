package main.api.response;

import main.api.request.PostRequest;
import main.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostResponse {

    @Autowired
    public PostResponse (PostRequest postRequest){}

    private int count;

    private List<Post> posts;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}

//{
//        "count": 390,
//        "posts": [
//          {
    //        "id": 345,
    //        "timestamp": 1592338706,
        //        "user":
        //        {
        //        "id": 88,
        //        "name": "Дмитрий Петров"
        //        },
    //        "title": "Заголовок поста",
    //        "announce": "Текст анонса поста без HTML-тэгов",
    //        "likeCount": 36,
    //        "dislikeCount": 3,
    //        "commentCount": 15,
    //        "viewCount": 55
    //        },
//        {...}
//        ]
//        }

