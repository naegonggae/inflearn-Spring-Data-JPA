package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedQuery;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@NamedQuery( // 솔직히 네임드쿼리 별로 안쓰는데 장점하나는 m.username 할때 필드명 틀리면 컴파일오류를 내준다.
		name = "Member.findByUsername", // 이름 포맷은 관례지만 아무렇게나 써도 상관없음
		query = "select m from Member m where m.username= :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity{

	@Id @GeneratedValue
	@Column(name = "member_id")
	private Long id;
	private String username;
	private int age;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

//	protected Member() { // 프록싱 등 사용하기 위해서 private 로 해놓으면 안된다.
//	}


	public Member(String username, int age) {
		this.username = username;
		this.age = age;
	}

	public Member(String username, int age, Team team) {
		this.username = username;
		this.age = age;
		if (team != null) {
			changeTeam(team);
		}
	}

	public Member(String username) {
		this.username = username;
	}

	public void changeUsername(String username) {
		this.username = username;
	}

	// 연관관계 편의 메서드
	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}
}
