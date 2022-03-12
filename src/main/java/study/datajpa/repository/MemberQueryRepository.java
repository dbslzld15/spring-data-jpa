package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {
    //항상 사용자 정의 리포지토리가 필요한 것은 아니다. 그냥 임의의 리포지토리를 만들어도 된다.
    //예를 들어 위와 같은 클래스를 스프링 빈으로 등록해서 직접 사용해도 된다.

    private final EntityManager em;

    List<Member> findAllMembers() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
