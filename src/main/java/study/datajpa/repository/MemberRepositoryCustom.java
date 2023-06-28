package study.datajpa.repository;

import java.util.List;
import study.datajpa.entity.Member;
// spring JPA 가 아니라 직접 구현한걸 쓰고 싶다.
public interface MemberRepositoryCustom {
	List<Member> findMemberCustom();

}
