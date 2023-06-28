package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

@Repository
public class MemberJpaRepository {

	@PersistenceContext
	private EntityManager em;

	public Member save(Member member) {
		em.persist(member);
		return member;
	}

	public void delete(Member member) {
		em.remove(member);
	}

	public List<Member> findAll() {
		return em.createQuery("select m from Member m", Member.class)
				.getResultList();
	}

	public Optional<Member> findById(Long id) {
		Member member = em.find(Member.class, id);
		return Optional.ofNullable(member);
	}

	public long count() {
		return em.createQuery("select count(m) from Member m", Long.class)
				.getSingleResult();
	}

	public Member find(Long id) {
		return em.find(Member.class, id);
	}

	public List<Member> findByUsernameAndAgeGreaterThen(String username, int age) {
		return em.createQuery("select m from Member m where m.username = :username and m.age > :age", Member.class)
				.setParameter("username", username)
				.setParameter("age", age)
				.getResultList();
	}

	// namedQuery 사용
	public List<Member> findByUsername(String username) {
		return em.createNamedQuery("Member.findByUsername", Member.class)
				.setParameter("username", username)
				.getResultList();
	}

	// 페이징
	// 주어진 age 인 member 를 이름기준 내림차순으로 정렬
	public List<Member> findByPage(int age, int offset, int limit) {
		return em.createQuery("select m from Member m where m.age=:age order by m.username desc")
				.setParameter("age", age)
				.setFirstResult(offset)
				.setMaxResults(limit)
				.getResultList();
	}

	// 페이징 카운트
	public long totalCount(int age) {
		return em.createQuery("select count(m) from Member m where m.age = :age", Long.class) // sorting 들어갈 필요없어서 안함
				.setParameter("age", age)
				.getSingleResult();
	}

}
