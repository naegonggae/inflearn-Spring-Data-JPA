package study.datajpa.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

	@Autowired MemberRepository memberRepository;

	@Test
	void testMember() {
		Member member = new Member("memberA");
		Member savedMember = memberRepository.save(member);

		Member findMember = memberRepository.findById(savedMember.getId()).get();
		//.get() 하면 Optional 를 까서 member 로 가져올 수 있음
		// 대신 null 이면 noSuchException 예외뜸 / 그래서 원래는 or else 이런식의 접근이 필요함
//		Optional<Member> byId = memberRepository.findById(savedMember.getId());
//		Member member1 = byId.get();

		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
		assertThat(findMember).isEqualTo(savedMember);
	}
}