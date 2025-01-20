package Jump.Service;


import Jump.Entity.Answer;
import Jump.Entity.Question;
import Jump.Entity.SiteUser;
import Jump.Repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Answer create(Question question,String content,SiteUser author){
        Answer answer=new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setAuthor(author);
        answer.setQuestion(question);
        answerRepository.save(answer);

        return answer;
    }


}
