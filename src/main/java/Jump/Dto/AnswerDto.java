package Jump.Dto;

import Jump.Entity.Answer;
import Jump.Entity.SiteUser;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnswerDto {
    private Integer id;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String user;
    private Integer votes;

    public AnswerDto(Answer answer){
        this.id= answer.getId();;
        this.content= answer.getContent();
        this.createDate=answer.getCreateDate();
        this.modifyDate=answer.getModifyDate();
        this.user= answer.getAuthor().getUsername();
        this.votes=answer.getVoter().size();
    }


}
