package Jump.Controller;

import Jump.Entity.*;
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
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.ReadOnlyFileSystemException;
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
    public String list(Model model, @RequestParam(value="page",defaultValue = "0") int page
    ,@RequestParam(value = "kw",defaultValue = "") String kw) {
        Page<Question> paging=questionService.getList(page,kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw",kw);
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm
                                 ,BindingResult bindingResult,@PathVariable("id") Integer id,Principal principal){
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        Question question=questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
        }

        questionService.modify(question,questionForm.getSubject(),questionForm.getContent());
        return String.format("redirect:/question/detail/%s",id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal,@PathVariable("id") Integer id){
        Question question=questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다.");
        }
        questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal,@PathVariable("id") Integer id){
        Question question=questionService.getQuestion(id);
        SiteUser siteUser=userService.getUser(principal.getName());

        questionService.vote(question,siteUser);
        return String.format("redirect:/question/detail/%s",id);
    }

}
