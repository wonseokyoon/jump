package Jump.Controller;

import Jump.Entity.AnswerForm;
import Jump.Entity.Question;
import Jump.Entity.QuestionForm;
import Jump.Entity.SiteUser;
import Jump.Service.QuestionService;
import Jump.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/question")
@Transactional
public class QuestionController {

    @Autowired
    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/id")
    public Optional<Question> findById(Long id){
        return questionService.findById(id);
    }

//    @GetMapping("/list")
//    public List<Question> findAll(){
//        return questionService.findAll();
//    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page",defaultValue = "0") int page) {
        Page<Question> paging=questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    @GetMapping(value="/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm){
        Question question=questionService.getQuestion(id);
        model.addAttribute("question",question);
        return "question_detail";
    }

    @PostMapping()
    public ResponseEntity<Question> createQuestion(@RequestBody Question question){
        return ResponseEntity.ok(questionService.createQuestion(question));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm){
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        SiteUser user= userService.getUser(principal.getName());
        questionService.create(questionForm.getSubject(),questionForm.getContent(),user);
        return "redirect:/question/list";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> modifyQuestion(@PathVariable Long id, @RequestBody Question modifyQuestion ){
        try{
            Question modifiedQuestion=questionService.modifyQuestion(id,modifyQuestion);
            return ResponseEntity.ok(modifiedQuestion);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @DeleteMapping("/id")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        try{
            questionService.deleteQuestion(id);
            return ResponseEntity.ok("Question"+id+" deleted Successfully");
        }catch (RuntimeException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
