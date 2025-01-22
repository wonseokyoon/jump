package Jump.Repository;

import Jump.Entity.Answer;
import Jump.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findByAnswer(Answer answer);

}
