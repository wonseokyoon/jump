package Jump.Service;


import Jump.Config.DataNotFoundException;
import Jump.Entity.Answer;
import Jump.Entity.Question;
import Jump.Entity.SiteUser;
import Jump.Repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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

    public Answer getAnswer(Integer id){
        Optional<Answer> answer=answerRepository.findById(id);
        if(answer.isPresent()){
            return answer.get();
        }else{
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(Answer answer,String content){
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        answerRepository.save(answer);
    }

    public void delete(Answer answer){
        answerRepository.delete(answer);
    }

    public void vote(Answer answer,SiteUser siteUser){
        answer.getVoter().add(siteUser);
        answerRepository.save(answer);
    }

}
