package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
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

	@GetMapping("/members")
	public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
//		Page<Member> page = memberRepository.findAll(pageable);
//		Page<MemberDto> map = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));
//		return map;
		PageRequest request = PageRequest.of(1, 2);
		// 페이지 리퀘스트를 만들고 page 형태로 내보내지말고 새로운 스타일형식으로 내보내는것 ex. MyPage<MemberDto> 요렇게
		return memberRepository.findAll(pageable)
//				.map(m -> new MemberDto(m));
				.map(MemberDto::new);

	}

	@PostConstruct
	public void init() {
		for (int i = 0; i<100; i++) {
			memberRepository.save(new Member("user"+i, i));

		}
	}

}
