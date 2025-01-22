package Jump.Dto;

import Jump.Entity.Question;
import Jump.Entity.QuestionForm;
import Jump.Entity.SiteUser;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionDto {
    private Integer id;
    private String subject;
    private String content;
    private LocalDateTime createDate;
    private SiteUser user;
    private List<AnswerDto> answerList;

    public QuestionDto(Question question){
        this.id = question.getId();
        this.subject = question.getSubject();
        this.content = question.getContent();
        this.createDate = question.getCreateDate();
        this.user=question.getAuthor();
        this.answerList = question.getAnswerList().stream()
                .map(AnswerDto::new)
                .toList();
    }
}
