package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberRepository memberRepository;

	@GetMapping("/members/{id}")
	public String findMember(@PathVariable("id") Long id) {
		Member member = memberRepository.findById(id).get();
		return member.getUsername();
	}

	@GetMapping("/members2/{id}") // 트랙잭션이 없는 상태에서 엔티티를 조회했기때문에 조회용도만 사용한다.
	public String findMember2(@PathVariable("id") Member member) { // 도메인 클래스 컨버터 사용 / 간단한 경우에만 사용가능
		return member.getUsername();
	}

	@PostConstruct
	public void init() {
		memberRepository.save(new Member("userA"));
	}

}
