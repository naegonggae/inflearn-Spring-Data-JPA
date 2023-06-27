package study.datajpa.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
