package study.datajpa.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

	@Autowired MemberRepository memberRepository;
	@Autowired TeamRepository teamRepository;

	@Test
	void testMember() {

		System.out.println("memberRepository = " + memberRepository.getClass());
		// memberRepository = class jdk.proxy2.$Proxy121
		// 개발자가 구현체를 만들지 않고 스프링 data JPA 가 JpaRepository extends 된걸 확인하고 구현클래스를 프록시객체로 만들어서 주입시켜줌
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

	@Test
	void basicCRUD() {
		Member member1 = new Member("memberA");
		Member member2 = new Member("memberB");
		memberRepository.save(member1);
		memberRepository.save(member2);

		// 단건조회 검증
		Member findMember1 = memberRepository.findById(member1.getId()).get();
		Member findMember2 = memberRepository.findById(member2.getId()).get();
		assertThat(findMember1).isEqualTo(member1);
		assertThat(findMember2).isEqualTo(member2);

		// 리스트 검증
		List<Member> all = memberRepository.findAll();
		assertThat(all.size()).isEqualTo(2);

		// 카운트 검증
		long count = memberRepository.count();
		assertThat(count).isEqualTo(2);

		// 삭제 검증
		memberRepository.delete(member1);
		memberRepository.delete(member2);
		long deletedCount = memberRepository.count();
		assertThat(deletedCount).isEqualTo(0);
	}

	@Test
	void findByUsernameAndAgeGreaterThan() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

		assertThat(result.get(0).getUsername()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(20);
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	void findHiBy() {
		List<Member> hiBy = memberRepository.findHiBy();
	}

	@Test
	void findTop3aaaaaBy() {
		Member member1 = new Member("AAAA", 10);
		Member member2 = new Member("AAAA", 10);
		Member member3 = new Member("AAAA", 10);
		Member member4 = new Member("AAAA", 10);
		memberRepository.save(member1);
		memberRepository.save(member2);
		memberRepository.save(member3);
		memberRepository.save(member4);

		List<Member> top3aaaaaBy = memberRepository.findTop3AaaaaBy();
		for (Member member : top3aaaaaBy) {
			System.out.println("member = " + member);
		}
	}

	@Test
	void testNamedQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> members = memberRepository.namedQuery("AAA");
		Member member = members.get(0);
		assertThat(member).isEqualTo(m1);
	}

	@Test
	void testQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> member = memberRepository.findUser("AAA", 10);
		assertThat(member.get(0)).isEqualTo(m1);
	}

	@Test
	void findUsernameList() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<String> list = memberRepository.findUsernameList();
		for (String s : list) {
			System.out.println("s = " + s);
		}
		assertThat(list.get(0)).isEqualTo("AAA");
	}

	@Test
	void findMemberDto() {
		Team team = new Team("teamA");
		teamRepository.save(team);

		Member m1 = new Member("AAA", 10);
		m1.changeTeam(team);
		memberRepository.save(m1);

		List<MemberDto> memberDto = memberRepository.findMemberDto();
		for (MemberDto dto : memberDto) {
			System.out.println("dto = " + dto);
		}
	}
}