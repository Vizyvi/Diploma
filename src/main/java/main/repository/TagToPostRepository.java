package main.repository;

import main.entity.TagToPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagToPostRepository  extends JpaRepository<TagToPost, Long> {
}
