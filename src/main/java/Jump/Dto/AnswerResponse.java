package Jump.Dto;

import Jump.Entity.Answer;
import Jump.Entity.Question;
import lombok.Data;

import java.util.List;

@Data
public class AnswerResponse {
    private List<AnswerDto> answerList;
    private Question question;

    public AnswerResponse(List<AnswerDto> answerList,Question question){
        this.answerList=answerList;
        this.question=question;
    }

}
