package Jump.Service;


import Jump.Entity.Answer;
import Jump.Entity.Comment;
import Jump.Entity.SiteUser;
import Jump.Repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment create(String content, SiteUser user, Answer answer){
        Comment comment=new Comment();
        comment.setContent(content);
        comment.setAuthor(user);
        comment.setCreateDate(LocalDateTime.now());
        comment.setAnswer(answer);
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentByAnswer(Answer answer){
        return commentRepository.findByAnswer(answer);
    }

}
