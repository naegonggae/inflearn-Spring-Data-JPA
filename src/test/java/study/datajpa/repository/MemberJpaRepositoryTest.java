package study.datajpa.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

@SpringBootTest // junit4의 RunWith 가 포함됨
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

	@Autowired MemberJpaRepository memberJpaRepository;
	@Autowired EntityManager em;

	@Test
	void testMember() {
		//given
		Member member = new Member("memberA");
		Member savedMember = memberJpaRepository.save(member);

		//when
		Member findMember = memberJpaRepository.find(savedMember.getId());

		//then
		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
		assertThat(findMember).isEqualTo(member);
	}

	@Test
	void basicCRUD() {
		Member member1 = new Member("memberA");
		Member member2 = new Member("memberB");
		memberJpaRepository.save(member1);
		memberJpaRepository.save(member2);

		// 단건조회 검증
		Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
		Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
		assertThat(findMember1).isEqualTo(member1);
		assertThat(findMember2).isEqualTo(member2);

		// 리스트 검증
		List<Member> all = memberJpaRepository.findAll();
		assertThat(all.size()).isEqualTo(2);

		// 카운트 검증
		long count = memberJpaRepository.count();
		assertThat(count).isEqualTo(2);

		// 삭제 검증
		memberJpaRepository.delete(member1);
		memberJpaRepository.delete(member2);
		long deletedCount = memberJpaRepository.count();
		assertThat(deletedCount).isEqualTo(0);
	}

	@Test
	void findByUsernameAndAgeGreaterThen() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		memberJpaRepository.save(m1);
		memberJpaRepository.save(m2);

		List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("AAA", 15);

		assertThat(result.get(0).getUsername()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(20);
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	void testNamedQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberJpaRepository.save(m1);
		memberJpaRepository.save(m2);

		List<Member> members = memberJpaRepository.findByUsername("AAA");
		Member member = members.get(0);
		assertThat(member).isEqualTo(m1);
	}

	@Test
	void testPaging() {
		//given
		memberJpaRepository.save(new Member("member1", 10));
		memberJpaRepository.save(new Member("member2", 10));
		memberJpaRepository.save(new Member("member3", 10));
		memberJpaRepository.save(new Member("member4", 10));
		memberJpaRepository.save(new Member("member5", 10));
		memberJpaRepository.save(new Member("member6", 10));

		int age = 10;
		int offset = 2; // 시작 숫자
		int limit = 4; // 가져올 숫자 (끝숫자가 아님)

		//when
		List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
		long totalCount = memberJpaRepository.totalCount(age);

		// 페이지 계산 공식 적용...
		// totalPage = totalCount / size ...
		// 마지막 페이지 ...
		// 최초 페이지 ...

		//then
		assertThat(members.size()).isEqualTo(4);
		assertThat(totalCount).isEqualTo(6);

	}

	@Test
	void bulkUpdate() {
		//given
		memberJpaRepository.save(new Member("member1", 10));
		memberJpaRepository.save(new Member("member2", 19));
		Member member3 = memberJpaRepository.save(new Member("member3", 20));
		memberJpaRepository.save(new Member("member4", 21));
		memberJpaRepository.save(new Member("member5", 40));
		
		//when
		int resultCount = memberJpaRepository.bulkAgePlus(20); // 영속성때문에 바로 안나옴 DB 에는 다 반영되어있음
		em.flush();
		em.clear();
		Member member = memberJpaRepository.findById(3l).get();

		//then
		assertThat(resultCount).isEqualTo(3);
		assertThat(member.getAge()).isEqualTo(21);
	}

}