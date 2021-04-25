package repository;

import model.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends CrudRepository<Tag, Integer> {
}
