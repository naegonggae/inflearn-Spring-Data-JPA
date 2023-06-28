package study.datajpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

@SpringBootTest
class ItemRepositoryTest {

	@Autowired ItemRepository itemRepository;

	@Test
	public void save() {
		Item item = new Item("A");
		itemRepository.save(item);
	}

}

// 리포지토리 구현체는 simple 리포지토리인데 그 안에 예를들어 save 를 보면 트랜잭션 어노테이션이 붙어있음 그래서 JPA 리포지토리 사용하고 트랜잭션이 안걸려있는
// 메서드나 클래스나 테스트에서 돌려도 리포지토리에 트랜잭션이 달린 save 이기 때문에 persist 하고 트랜잭션 종료할때 commit 하니까 insert 가 나감
// 근데 주의할게 save 로직은 새로운객체면 persist, 아니면 select 로 찾아보고 merge 를 함
// 근데 새로운객체인지 찾는 방법은 객체가 Null 이거나 기본형이면 0 이여함 그래서 id 를 자동으로 붙여주는 전략은 새로운 객체를 save 호출할때 새로운객체로 인식함
// 근데 id 자동 매칭안하고 내가 임의값을 매칭해주면 save 는 이거 새로운객체 아니구나 생각하고 db 에서 select 날리고 찾아올 생각을함, 근데 당연히 없으니 merger 를하고 commit 되면서 insert 된
// 아주 merge 가 되면 select 를 새로운값임에도 불구하고 날리기 때문에 굉장히 비효율적임
// 그럼 어쩌라고 item 에 Persistable implement 하고 CreatedDate 가 null 이면 새로운 객체로 보게 한다.