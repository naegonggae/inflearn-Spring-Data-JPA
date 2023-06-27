package study.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {

	@Id @GeneratedValue
	private Long id;
	private String username;

	protected Member() { // 프록싱 등 사용하기 위해서 private 로 해놓으면 안된다.
	}

	public Member(String username) {
		this.username = username;
	}

	public void changeUsername(String username) {
		this.username = username;
	}
}
