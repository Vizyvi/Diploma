package main.repository;

import main.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAll(Pageable pageable);

    @Query(value = "SELECT p FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
            "ORDER BY p.time DESC")
    Page<Post> findAllRecent(Pageable pageable);

    @Query(value = "SELECT p FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
            "ORDER BY p.time")
    Page<Post> findAllEarly(Pageable pageable);

    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN PostComment pc ON pc.postId = p.id\n" +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pc) DESC")
    Page<Post> findAllPopular(Pageable pageable);

    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN PostVote pv ON pv.postId = p.id\n" +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pv) DESC")
    Page<Post> findAllBest(Pageable pageable);

    @Query(value = "SELECT * FROM Posts\n" +
                "WHERE is_active = ?1 AND moderation_status = ?2 AND time <= ?3\n" +
            "ORDER BY time DESC\n" +
            "LIMIT ?4, ?5", nativeQuery = true)
    List<Post> findAllByActiveAndStatusAndTimeRecent(boolean isActive, String status, String time, Integer offset, Integer limit);

    @Query(value = "SELECT * FROM Posts\n" +
            "WHERE is_active = ?1 AND moderation_status = ?2 AND time <= ?3 " +
            "ORDER BY time " +
            "LIMIT ?4, ?5", nativeQuery = true)
    List<Post> findAllByActiveAndStatusAndTimeEarly(boolean isActive, String status, String time, Integer offset, Integer limit);

    @Query(value = "SELECT * FROM Posts p\n" +
            "LEFT JOIN (SELECT post_id, count(id) as cId FROM post_comments\n" +
            "GROUP BY post_id) pc ON pc.post_id = p.id\n" +
            "WHERE is_active = ?1 AND moderation_status = ?2 AND time <= ?3\n" +
            "ORDER BY pc.cId DESC\n" +
            "LIMIT ?4, ?5", nativeQuery = true)
    List<Post> findAllByActiveAndStatusAndTimePopular(boolean isActive, String status, String time, Integer offset, Integer limit);

    @Query(value = "SELECT * FROM Posts p\n" +
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

    @Query(value = "SELECT * FROM Posts\n" +
            "WHERE title LIKE ?1\n" +
            "AND is_active = true AND moderation_status = 'ACCEPTED' AND now() >= time\n" +
            "LIMIT ?2, ?3", nativeQuery = true)
    List<Post> getPostsByQuery(String query, Integer offset, Integer limit);

    @Query(value = "SELECT * FROM Posts\n" +
            "WHERE time = ?1\n" +
            "AND is_active = true AND moderation_status = 'ACCEPTED' AND now() >= time\n" +
            "LIMIT ?2, ?3", nativeQuery = true)
    List<Post> getPostsByDate(String date, Integer offset, Integer limit);

    @Query(value = "SELECT p.* FROM Posts p\n" +
            "JOIN tag2post tp ON tp.post_id = p.id\n" +
            "JOIN tags t ON tp.tag_id = t.id\n" +
            "WHERE t.name LIKE ?1\n" +
            "AND is_active = true AND moderation_status = 'ACCEPTED' AND now() >= time\n" +
            "LIMIT ?2, ?3", nativeQuery = true)
    List<Post> getPostsByTag(String tag, Integer offset, Integer limit);

    @Query(value = "SELECT * FROM Posts\n" +
            "WHERE is_active = true AND moderation_status = 'ACCEPTED' AND now() >= time\n" +
            "LIMIT ?1, ?2", nativeQuery = true)
    List<Post> getAllPosts(Integer offset, Integer limit);

    @Query(value = "SELECT * FROM Posts\n" +
            "WHERE id = ?1 \n", nativeQuery = true)
    Post getOne(Long id);

    @Query(value = "SELECT DATE(p.time), COUNT(p.id) FROM Posts p\n" +
            "   JOIN (\n" +
            "       SELECT time FROM Posts\n" +
            "       WHERE YEAR(time) = ?1\n" +
            "       GROUP BY time\n" +
            "       ) pt ON pt.time = p.time\n" +
            "GROUP BY p.time\n" +
            "ORDER BY p.time", nativeQuery = true)
    List<String[]> findAllActiveByYear(Integer year);

    @Query(value =  "SELECT YEAR(p.time) AS years FROM Posts p\n" +
                    "GROUP BY YEAR(p.time)\n" +
                    "ORDER BY years", nativeQuery = true)
    List<Integer> findAllYearsWithPosts();

    @Query(value = "SELECT p FROM Post p WHERE p.moderationStatus = 'NEW'")
    List<Post> findAllNewPostsForModerator();

    @Query(value = "SELECT p FROM Post p " +
            "WHERE p.userId = ?1 AND p.isActive = false ")
    Page<Post> findMyPostsInactive(Integer userId, Pageable pageable);

    @Query(value = "SELECT p FROM Post p " +
            "WHERE p.userId = ?1 AND p.isActive = true AND p.moderationStatus = 'NEW' ")
    Page<Post> findMyPostsPending(Integer userId, Pageable pageable);

    @Query(value = "SELECT p FROM Post p " +
            "WHERE p.userId = ?1 AND p.isActive = true AND p.moderationStatus = 'DECLINED' ")
    Page<Post> findMyPostsDeclined(Integer userId, Pageable pageable);

    @Query(value = "SELECT p FROM Post p " +
            "WHERE p.userId = ?1 AND p.isActive = true AND p.moderationStatus = 'ACCEPTED'")
    Page<Post> findMyPostsPublished(Integer userId, Pageable pageable);
}
