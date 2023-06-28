package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
	// MemberRepositoryImpl Impl 빼고 JPA 가 만들어주는 MemberRepository 와 이름이동일해야한다. MemberRepositoryCustom 이거는 상관없어
	// 뒤에 Impl 까지 써준기

	private final EntityManager em;

	@Override
	public List<Member> findMemberCustom() {
		return em.createQuery("select m from Member m")
				.getResultList();
	}
}
// 사용시기
//1. JPA 로 충분하냐
//2. 복잡한 쿼리, 동적쿼리를 사용해야한다 하면 쿼리 DSL 처럼 사용자 정의 리포지토리를 사용한다.
//3. 뭐 진짜 필요하다 싶으면 그냥 클래스에 아무이름 지어서 리포지토리 어노테이션 붙이고 사용하면됨
// JDBC 템플릿 구현할때도 사용자 정의 리포지토리 사용
// 그리고 핵심 비즈니스 로직이 있는 리포지토리랑 화면을 위한 로직이 있는 리포지토리랑 분리(클래스를 쪼개버린다.)를 해놔야한다.

// 커맨드와 쿼리를 분리
// 핵심비즈니스로직과 화면구성로직을 분리
