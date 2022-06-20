package spring.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import spring.datajpa.dto.MemberDto;
import spring.datajpa.entity.Member;
import spring.datajpa.entity.Team;
import spring.datajpa.repository.MemberRepository;
import spring.datajpa.repository.TeamRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @Autowired
    @PersistenceContext
    EntityManager em;

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

    @Test
    public void paging(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 50));

        int age = 10;

        // spring data jpa는 페이지를 0 부터 시작
        // PageRequest의 부모 인터페이스에 pageable이 있다
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // Map을 쓰면 내부의 것을 바꾸어 다른 결과를 낼 수 있다.
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName()));

        // totalcount는 가져올 필요가 없다. 반환타입 page에 이미 포함됨
        // long totalCount = memberRepository.totalCount(age);
        //then

        // 페이징 해온 애를 조회
        List<Member> content = page.getContent();
        //long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }
        //System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        //assertThat(page.getTotalElements()).isEqualTo(4);
        assertThat(page.getNumber()).isEqualTo(0);
        //assertThat(page.getTotalPages()).isEqualTo(2); // 총 페이지 개수
        assertThat(page.isFirst()).isTrue(); // 첫 번째 페이지인지
        assertThat(page.hasNext()).isTrue(); // 다음 페이지 있는지
    }

    @Test
    public void bulkUpdate(){

        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when : 20이상인 사람의 나이 +1
        int resultCount = memberRepository.bulkAgePlus(20);

        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy(){
        //given : member1 -> teamA, member2 -> teamB
        // member - team은 다대일 관계. lazy 로 설정되어있음.
        // team은 가짜 객체로 조회 해놓고 실제 사용 시점에 team 쿼리 날림

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // N+1 이슈 발생중
        // select Member : 쿼리 한 개
        // team은 조회된 멤버에 따라 추가쿼리가 N개 발생
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            // 여기서는 member만 조회함. team 쿼리는 안나감
            System.out.println("member.getUsername() = " + member.getUsername());

            // 이렇게 조회할 경우 getTeam시 프록시가 조회됨
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());

            // 지금 team getName 시에 실제 Db 쿼리가 날라감 (=프록시가 초기화됨)
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());

        }
    }

    @Test
    public void queryHint(){
        // given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        //when
        // 변경 안하고, find만 해서 조회만 할거라도, find 하는 순간 스냅샷을 만들어버림
        // 그래서 jpa hint를 통해 read only true로 설정하여 조회시엔 스냅샷 만들지 않도록 함
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");
        em.flush(); // // 변경 감지가 되어 flush 후 db에 업데이트 쿼리가 나간다.
    }

    @Test
    public void lock(){
        // given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        //when
        List<Member> result = memberRepository.findLockByUsername("member1");

    }

    @Test
    public void callCustom(){
        List<Member> members = memberRepository.findMemberCustom();

    }

}

