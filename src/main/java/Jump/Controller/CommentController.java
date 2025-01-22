package Jump.Controller;


import Jump.Entity.*;
import Jump.Repository.AnswerRepository;
import Jump.Service.AnswerService;
import Jump.Service.CommentService;
import Jump.Service.QuestionService;
import Jump.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@Transactional
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private AnswerService answerService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/{answerId}")
    public String commentCreate(Model model, @PathVariable("answerId") Integer answerId){
        Answer answer= answerService.getAnswer(answerId);
        model.addAttribute("answer",answer);
        model.addAttribute("commentForm",new CommentForm());
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{answerId}")
    public String commentCreate(@Valid CommentForm commentForm
                                ,BindingResult bindingResult
                                ,Principal principal
                                ,@PathVariable("answerId") Integer answerId){
        Answer answer= answerService.getAnswer(answerId);
        if(bindingResult.hasErrors()){
            return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
        }
        SiteUser user= userService.getUser(principal.getName());
        commentService.create(commentForm.getContent(),user,answer);
        return String.format("redirect:/question/detail/%s",answer.getQuestion().getId());
    }

    @GetMapping("/list/{answerId}")
    public String listComments(@PathVariable("answerId") Integer answerId,
                               Model model){
        Answer answer= answerService.getAnswer(answerId);
        List<Comment> commentList=commentService.getCommentByAnswer(answer);
        model.addAttribute("commentList",commentList);
        model.addAttribute("answer",answer);
        return "comment_list";
    }

}
