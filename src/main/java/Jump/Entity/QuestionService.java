package Jump.Entity;

import Jump.DataNotFoundException;
import Jump.Repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Optional<Question> findById(long id) {
        return questionRepository.findById(id);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question modifyQuestion(Long id,Question modifyQuestion) {
        Optional<Question> optionalQuestion=questionRepository.findById(id);
        String subject=modifyQuestion.getSubject();
        String content= modifyQuestion.getContent();

        if(optionalQuestion.isPresent()){
            Question question=optionalQuestion.get();
            question.setSubject(subject);
            question.setContent(content);
            return questionRepository.save(question);
        }
        else {
            throw new RuntimeException("존재하지 않는 Question");
        }
    }

    public void deleteQuestion(Long id) {
        try {
            Optional<Question> question = questionRepository.findById(id);
            Question deleteQuestion = question.get();
            questionRepository.delete(deleteQuestion);
        } catch (RuntimeException e){
            throw new RuntimeException("Question"+id+" is not found");
        }
    }

    public Question createQuestion(Question question) {
        question.setCreateDate(LocalDateTime.now());
        return questionRepository.save(question);

    }

    public Question getQuestion(Integer id){
        Optional<Question> question=questionRepository.findById(id);
        if(question.isPresent()){
            return question.get();
        }else {
            throw new DataNotFoundException("question not found");
        }
    }
}
