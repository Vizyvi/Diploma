package main.repository;


import main.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagsRepository extends JpaRepository<Tag, Long> {

    @Query(value = "SELECT * FROM tags WHERE name LIKE ?1", nativeQuery = true)
    List<Tag> getAllByQuery(String query);

    @Query(value = "SELECT MAX(cnt) FROM\n" +
            "(SELECT COUNT(id) as cnt FROM tag2post GROUP BY tag_id ) as countValues", nativeQuery = true)
    Integer getCountMostPopularTag();

    @Query(value = "SELECT t.name, COUNT(tp.id)  \n" +
            "FROM tags t\n" +
            "LEFT JOIN tag2post tp ON t.id = tp.tag_id\n" +
            "WHERE t.name IN ?1 \n" +
            "GROUP BY tag_id", nativeQuery = true)
    List<Object[]> getCountForListName(List<String> tagNames);

    @Query(value = "SELECT t FROM Tag t WHERE t.name = ?1")
    Optional<Tag> findByName(String tagName);
}
