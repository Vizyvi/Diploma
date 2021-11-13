package main.repository;

import main.entity.ModerationStatus;
import main.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT * FROM Posts\n" +
                "WHERE is_active = ?1 AND moderation_status = ?2 AND time <= ?3\n" +
            "ORDER BY time DESC\n" +
            "LIMIT ?4, ?5", nativeQuery = true)
    List<Post> findAllByActiveAndStatusAndTimeRecent(boolean isActive, String status, String time, Integer offset, Integer limit);

    @Query(value = "SELECT * FROM Posts\n" +
            "WHERE is_active = ?1 AND moderation_status = ?2 AND time <= ?3" +
            "ORDER BY time" +
            "LIMIT ?4, ?5", nativeQuery = true)
    List<Post> findAllByActiveAndStatusAndTimeEarly(boolean isActive, String status, String time, Integer offset, Integer limit);

    @Query(value = "SELECT * FROM Posts\n" +
            "LEFT JOIN (SELECT post_id, count(id) as cId FROM post_comments\n" +
            "GROUP BY post_id) pc ON pc.post_id = p.id\n" +
            "WHERE is_active = ?1 AND moderation_status = ?2 AND time <= ?3\n" +
            "ORDER BY pc.cId DESC\n" +
            "LIMIT ?4, ?5", nativeQuery = true)
    List<Post> findAllByActiveAndStatusAndTimePopular(boolean isActive, String status, String time, Integer offset, Integer limit);

    @Query(value = "SELECT * FROM Posts\n" +
            "LEFT JOIN (SELECT post_id, count(id) as likes FROM post_votes\n" +
            "GROUP BY post_id) pv ON pv.post_id = p.id\n" +
            "WHERE is_active = ?1 AND moderation_status = ?2 AND time <= ?3\n" +
            "ORDER BY pv.likes DESC\n" +
            "LIMIT ?4, ?5", nativeQuery = true)
    List<Post> findAllByActiveAndStatusAndTimeBest(boolean isActive, String status, String time, Integer offset, Integer limit);

    @Query(value = "SELECT \n" +
            "IF(pv.value = true, 1,0) AS likes,\n" +
            "IF(pv.value = false, 1,0) AS dislikes,\n" +
            "count(pc.id) AS comments\n" +
            "FROM Posts p\n" +
            "LEFT JOIN post_votes pv ON pv.post_id = p.id\n" +
            "LEFT JOIN post_comments pc ON pc.post_id = p.id\n" +
            "WHERE p.id = ?1\n" +
            "GROUP BY p.id" , nativeQuery = true)
    List<Object[]> getPostInfo(Long postId);

//    List<Post> findAllByActiveAndModerationStatusAndTime(boolean isActive, ModerationStatus moderationStatus, Date time);
}
