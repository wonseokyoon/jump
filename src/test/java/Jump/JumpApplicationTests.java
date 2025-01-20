package Jump;

import Jump.Controller.QuestionController;
import Jump.Entity.Question;
import Jump.Repository.QuestionRepository;
import Jump.Service.QuestionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class JumpApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private QuestionController questionController;
	@Autowired
	private QuestionService questionService;
	@Test
	@DisplayName("create table")
	void testJpa1() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);  // 첫번째 질문 저장

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);  // 두번째 질문 저장
	}

	@Test
	@DisplayName("findAll")
	void testJpa2() {
		List<Question> questionList=questionRepository.findAll();
		assertEquals(2,questionList.size());

		Question question=questionList.get(0);
		assertEquals("sbb가 무엇인가요?",question.getSubject());

	}

	@Test
	@DisplayName("findById")
	void testJpa3(){
		Optional<Question> question=questionRepository.findById(1);
		// 존재하는지 확인
		assertThat(question).isPresent();

		// 필드 검증
		question.ifPresent(q -> {
			assertThat(q.getSubject())
					.isEqualTo("sbb가 무엇인가요?");
			assertThat(q.getContent())
					.isEqualTo("sbb에 대해서 알고 싶습니다.");
		});
	}

	@Test
	@DisplayName("findBySubject")
	void testJpa4(){
		Optional<Question> question=questionRepository.findBySubject("sbb가 무엇인가요?");
		// 존재하는지 확인
		assertThat(question).isPresent();

		// 필드 검증
		question.ifPresent(q -> {
			assertThat(q.getId())
					.isEqualTo(1);
			assertThat(q.getContent())
					.isEqualTo("sbb에 대해서 알고 싶습니다.");
		});
	}

	@Test
	@DisplayName("findBySubjectAndContent")
	void testJpa5(){
		Optional<Question> question
				=questionRepository.findBySubjectAndContent("sbb가 무엇인가요?","sbb에 대해서 알고 싶습니다.");
		// 존재하는지 확인
		assertThat(question).isPresent();

		// 필드 검증
		question.ifPresent(q -> {
			assertThat(q.getId())
					.isEqualTo(1);
			assertThat(q.getContent())
					.isEqualTo("sbb에 대해서 알고 싶습니다.");
			assertThat(q.getSubject())
					.isEqualTo("sbb가 무엇인가요?");
		});
	}

	@Test
	@DisplayName("findBySubjectLike")
	void testJpa6() {
		List<Question> list=questionRepository.findBySubjectLike("sbb%");

		// 0번 question이 sbb문자열로 시작하는지 확인
		Question question=list.get(0);
		assertThat(question.getSubject())
				.contains("sbb");
	}

	@Test
	@DisplayName("modfiy question")
	void testJpa7() {
		Optional<Question> question=questionRepository.findById(1);

		// 존재하는지
		assertThat(question)
				.isPresent();
		// 수정 전
		Question question1=question.get();
		assertThat(question1.getSubject())
				.isEqualTo("sbb가 무엇인가요?");

		// 수정 후
		question1.setSubject("수정된 제목");
		assertThat(question1.getSubject())
				.isEqualTo("수정된 제목");
		//db 저장
		questionRepository.save(question1);

	}

	@Test
	@DisplayName("delete question")
	void testJpa8() {
		Long idToDelete = 1L;
		Question question = new Question();
		question.setId(idToDelete);
		question.setSubject("삭제할 질문");
		question.setContent("이 질문은 삭제될 것입니다.");
		question.setCreateDate(LocalDateTime.now());
		questionRepository.save(question);

		Optional<Question> beforeDelete = questionRepository.findById(idToDelete);
		assertThat(beforeDelete).isPresent();

		// 삭제 요청을 수행
		ResponseEntity<String> response = questionController.deleteQuestion(idToDelete);

		// 응답 검증
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Question" + idToDelete + " deleted Successfully", response.getBody());

		// 삭제 후 질문이 존재하지 않는지 확인
		Optional<Question> deletedQuestion = questionRepository.findById(idToDelete);
		assertFalse(deletedQuestion.isPresent(), "질문이 삭제되어야 합니다.");
	}

	@Test
	@DisplayName("create question")
	void testJpa9(){
		String subject = "테스트 제목";
		String content = "테스트 내용";
		Question newQuestion=new Question();
		newQuestion.setSubject(subject);
		newQuestion.setContent(content);

		ResponseEntity<Question> response
				=questionController.createQuestion(newQuestion);
		assertEquals(HttpStatus.OK,response.getStatusCode());
		Optional<Question> optionalQuestion=questionController.findById(newQuestion.getId());
		assertThat(optionalQuestion).isPresent();
	}

	@Test
	void testJpa10() {
		for (int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";
			this.questionService.create(subject, content,null);
		}
	}

}
