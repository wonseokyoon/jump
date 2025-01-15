package Jump;

import Jump.Entity.Question;
import Jump.Repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JumpApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

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

}
