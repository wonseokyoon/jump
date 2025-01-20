package Jump.Controller;


import Jump.Entity.AnswerForm;
import Jump.Entity.Question;
import Jump.Entity.SiteUser;
import Jump.Service.AnswerService;
import Jump.Service.QuestionService;
import Jump.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    @Autowired
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @Valid AnswerForm answerForm, BindingResult bindingResult
    , Principal principal){
        Question question=questionService.getQuestion(id);
        SiteUser siteUser=userService.getUser(principal.getName());
        if(bindingResult.hasErrors()){
            model.addAttribute("question",question);
            return "question_detail";
        }
        answerService.create(question,answerForm.getContent(),siteUser);
        return String.format("redirect:/question/detail/%s",id);
    }

}
