package Jump;

import Jump.Entity.Question;
import Jump.Entity.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/id")
    public Optional<Question> findById(Long id){
        return questionService.findById(id);
    }

    @GetMapping
    public List<Question> findAll(){
        return questionService.findAll();
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
}
