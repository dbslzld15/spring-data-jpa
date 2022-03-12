package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);
        //when
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void CRUD검증() throws Exception {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        //then
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //when
        List<Member> all = memberRepository.findAll();
        //then
        assertThat(all.size()).isEqualTo(2);

        //when
        long count = memberRepository.count();
        //then
        assertThat(count).isEqualTo(2);

        //when
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        //then
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThanTest() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        //then
        assertThat(members.get(0).getUsername()).isEqualTo("AAA");
        assertThat(members.get(0).getAge()).isEqualTo(20);
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        List<Member> members = memberRepository.findByUsername("AAA");
        Member findMember = members.get(0);
        //then
        assertThat(findMember).isEqualTo(member1);
    }

    @Test
    public void testQuery() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        List<Member> members = memberRepository.findMember("AAA", 10);
        Member findMember = members.get(0);
        //then
        assertThat(findMember).isEqualTo(member1);
    }

    @Test
    public void findUsernameList() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        List<String> usernameList = memberRepository.findUsernameList();
        //then
        assertThat(usernameList.get(0)).isEqualTo("AAA");
    }

    @Test
    public void findMemberDto() throws Exception {
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("AAA", 10);
        memberRepository.save(member1);
        member1.setTeam(team);

        //when
        List<MemberDto> memberDto = memberRepository.findMemberDto();
        //then
        assertThat(memberDto.get(0).getTeamName()).isEqualTo("teamA");
    }

    @Test
    public void findByNames() {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        List<Member> members = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        //then
        assertThat(members.get(0).getUsername()).isEqualTo("AAA");
        assertThat(members.get(1).getUsername()).isEqualTo("BBB");
    }

    @Test
    public void returnType() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        Member member3 = new Member("BBB", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        //when
        Member findMember = memberRepository.findMemberByUsername("AAAB"); // 만약 찾고자하는 데이터가 없을 경우 단건조회의 경우 null값이 들어감
        List<Member> members = memberRepository.findListByUsername("AAAB"); // 만약 찾고자하는 데이터가 없을경우 컬렉션은 null이 아니라 empty임
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAAB"); //그렇기에 단건조회의 경우 Optional을 사용

        // Member bMember = memberRepository.findMemberByUsername("BBB"); //이 부분은 다건이 조회되기때문에 예외가 터짐

        //then

    }
}