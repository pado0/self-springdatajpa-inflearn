package spring.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import spring.datajpa.dto.MemberDto;
import spring.datajpa.entity.Member;
import spring.datajpa.entity.Team;
import spring.datajpa.repository.MemberRepository;
import spring.datajpa.repository.TeamRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    public void findMemberBy(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        Member m3 = new Member("AAA", 30);
        Member m4 = new Member("AAA", 40);

        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);

        List<Member> memberBy = memberRepository.findTop3MemberBy();
        for (Member member : memberBy) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void testQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        Member m3 = new Member("AAA", 30);
        Member m4 = new Member("AAA", 40);

        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUserNameList(){
        Member m1 = new Member("AAA1", 10);
        Member m2 = new Member("AAA2", 20);
        Member m3 = new Member("AAA3", 30);
        Member m4 = new Member("AAA4", 40);

        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void findMemberDto(){

        // 멤버와 팀 세팅
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA1", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto s : memberDto) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void findByNames(){

        Member m1 = new Member("AAA1", 10);
        Member m2 = new Member("AAA2", 20);
        Member m3 = new Member("AAA3", 20);
        Member m4 = new Member("AAA4", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);


        // 이름 리스트를 기반으로 레퍼지토리 조회
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA1", "AAA2"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType(){
        Member m1 = new Member("AAA1", 10);
        Member m2 = new Member("AAA2", 20);
        Member m3 = new Member("AAA3", 20);
        Member m4 = new Member("AAA4", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);

        // 리스트 리턴. 조회된 값이 없을 경우 빈 컬렉션이 내려옴
        // null 예외처리 안해도 됨
        List<Member> aaa4 = memberRepository.findListByUsername("AAA4");
        for (Member member : aaa4) {
            System.out.println("member = " + member);
        }

        // 단건 리턴
        // 단건처리가 Null일 수 있어짐. 이 경우 null을 뱉어버림.
        // 그냥 옵셔널을 쓰는게 낫다.!!!
        Member aaa2 = memberRepository.findMemberByUsername("AAA2");
        System.out.println("aaa2 = " + aaa2);

        // 옵셔널 리턴
        Optional<Member> aaa3 = memberRepository.findOptionalMemberByUsername("AAA20");
        System.out.println("aaa3 = " + aaa3);

    }
}
