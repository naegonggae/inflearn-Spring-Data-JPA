package study.datajpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

	// 파라미터를 컬랙션으로 바인딩하는거 / In 절로 받아옴
	@Query("select m from Member m where m.username in :names")
	List<Member> findByNames(@Param("names") Collection<String> names);

	List<Member> findListByUsername(String username); // 컬랙션
	Member findMemberByUsername(String username); // 단건
	Optional<Member> findOptionalByUsername(String username); // optional 단건

	// DB 가 단순하면 Page 에서 해주는 토탈카운트 믿고 가면되는데 복잡해지면 이렇게 쿼리를 분리해줘야한다. / 성능테스트해서 느리다하면 고치자
	@Query(value = "select m from Member m left join m.team t",
			countQuery = "select count(m) from Member m") // 토탈카운트 쿼리를 날릴때는 조인을 할 필요가 없기 때문에 카운트전용 별도의 쿼리를 작성
	Page<Member> findByAge(int age, Pageable pageable); // 간단하게 3건만 넘기고 싶다하면 페이징안하고 Top3해도 된다.
//	Slice<Member> findByAge(int age, Pageable pageable);

	@Modifying(clearAutomatically = true)
	@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
	int bulkAge(@Param("age") int age);

	// JPQL 로 페치조인 구현
	@Query("select m from Member m left join fetch m.team") //** 아니면 요거픽
	List<Member> findMemberFetchJoin();

	// 엔티티그래프로 페치조인 구현
	@Override
	@EntityGraph(attributePaths = "team")
	List<Member> findAll();

	// 위의 짬뽕 구현 = 페치조인
	@EntityGraph(attributePaths = "team")
	@Query("select m from Member m") // 조인 뺀 쿼리
	List<Member> findMemberEntityGraph();

	// 메서드명으로 커스텀 할때도 엔티티그래프사용해서 페치조인할 수 있음
	@EntityGraph(attributePaths = "team") //** 간다한게 하고 싶을때 요거 픽
	List<Member> findFetchByUsername(@Param("username") String username);

	// 네임드 쿼리도 가능 페치조인이
	@EntityGraph("Member.all")
	List<Member> findFetchJoinByUsername(@Param("username") String username);
}
