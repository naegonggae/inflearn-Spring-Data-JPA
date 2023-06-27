package study.datajpa.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	List<Member> findByUsernameAndAgeGreaterThan(String username, int age); // 쿼리 메서드 기능 그냥 알아서 JPA 가 만들어줌
	// AgeGreaterThan age 보다 크면
	// findByUsername username = username 포함된걸 리스트 형태로 뽑음
	// 근데 이 방법은 조건이 두개이상 들어가면 너무 난잡해짐 조건 두개이상은 뒤에 나올 JPQL 짜는 방식 사용권장

	List<Member> findHiBy();
	// Hi는 아무상관이 없어 그리고 By 뒤에 조건이 오는데 아무것도 없으니 전체 조회임

	List<Member> findTop3AaaaaBy();
	// 문자 아무렇게 쳐도 대문자로 구분을 해줘야함
	// Top3 = 처음부터 3개만 가져옴

	// 필드이름 바꼈을때 에러로 잡아준다는 장점이 있다.

	// @Query 를 주석처리해도 네임드 쿼리가 실행되는데 그때는 JPA 동작 우선순위가 설정된 엔티티에 점찍고 메서드명으로 된 네임드쿼리를 먼저 찾고 없으면 다른걸함
	@Query(name = "Member.findByUsername")
	List<Member> namedQuery(@Param("username") String username);

	// 메서드에 JPQL 바로 꽂아넣기 / 아주 막강한 기술, 실무에서 사용됨 / 장점으로 필드명 오류시 컴파일 오류로 잡아줌
	// 1. 간단한 쿼리는 일반 메서드로 만들고 2. 복잡한 정적쿼리면 이렇게 만들자 3. 동적쿼리는 쿼리 DSL 로...
	@Query("select m from Member m where m.username = :username and m.age = :age")
	List<Member> findUser(@Param("username") String username, @Param("age") int age);

	@Query("select m.username from Member m")
	List<String> findUsernameList();

	// DTO 를 반환할때는 이렇게 해줘야함
	@Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
	List<MemberDto> findMemberDto();
}
