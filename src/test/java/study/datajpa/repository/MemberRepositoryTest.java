package study.datajpa.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
	@PersistenceContext EntityManager em;

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

	@Test
	void findByNames() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
		for (Member member : result) {
			System.out.println("member = " + member);
		}
	}

	@Test
	void returnType() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20); // 이거도 "AAA" 였으면 단건조회에서 예외터짐
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result1 = memberRepository.findListByUsername("CCC");
		System.out.println("result1.size() = " + result1.size()); // size = 0
		// 이거는 값이 없어도 null 이 안되고 빈 배열을 출력해준다. 그래서 if 로 null 조회 안해도됨
		Member result2 = memberRepository.findMemberByUsername("CCC");
		System.out.println("result2 = " + result2); // null
		Optional<Member> result3 = memberRepository.findOptionalByUsername("CCC");
		System.out.println("result3 = " + result3); // Optional.empty / 값이 비어있으면 .orElse 로 넘기면된다.
		// 결과가 없을수도 있고 있을수도 있으면 optional 을 써라
	}

	@Test
	void testPaging() {
		//given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		memberRepository.save(new Member("member5", 10));
		memberRepository.save(new Member("member6", 10));

		int age = 10;
		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));
		// sorting 조건도 너무 복잡해지면 위와 같이 하지말고 레포지토리에 쿼리를 작성하자

		//when
		Page<Member> page = memberRepository.findByAge(age, pageRequest);
//		Slice<Member> page = memberRepository.findByAge(age, pageRequest); //size 에서 하나더 가져옴, 토탈카운트를 지원하지 않음
		// 그냥 List 로 설정해서 몇개 가져오고 sorting 할 수 있음

		// 페이징할때 엔티티를 외부에 노출하면 안된다. 그래서 DTO 로 잡아야하는데 쉽게 변환하는 방법은
		Page<MemberDto> toMap = page.map(
				member -> new MemberDto(member.getId(), member.getUsername(), null));

		//then
		List<Member> content = page.getContent(); // 페이징한것들 뽑아서 옴
		long totalElements = page.getTotalElements(); // 토탈 카운트 지원
		for (Member member : content) {
			System.out.println("member = " + member);
		}
		System.out.println("totalElements = " + totalElements);

		assertThat(content.size()).isEqualTo(3);
		assertThat(page.getTotalElements()).isEqualTo(6);
		assertThat(page.getNumber()).isEqualTo(0);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.isFirst()).isTrue(); // 첫페이지 인지
		assertThat(page.hasNext()).isTrue(); // 다음페이지 인지

	}

	@Test
	void bulkUpdate() {
		//given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 19));
		Member member3 = memberRepository.save(new Member("member3", 20));
		memberRepository.save(new Member("member4", 21));
		memberRepository.save(new Member("member5", 40));

		//when
		// 심지어 JPQL 실행되기전에 이전것들은 flush 해줌
		int resultCount = memberRepository.bulkAge(20); // 영속성컨텍스트를 무시하고 바로 DB 에 값을 때려버림, 여기 영속성컨텍스트는 그 사실을 모름
//		em.flush();
//		em.clear();
		Member member = memberRepository.findById(3l).get();

		//then
		assertThat(resultCount).isEqualTo(3);
		assertThat(member.getAge()).isEqualTo(21);
	}
}