package spring.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.datajpa.dto.MemberDto;
import spring.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

// 인터페이스고, 구현체가 없다. 내부 함수도 인터페이스만 있는데, 구현체가 없이 어떻게 이 기능이 동작할까?

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 인터페이스만 만들어줌.
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3MemberBy();

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // dto 조회시, 마치 new로 생성하여 반환하는 것 처럼 작성을 해야함.
    // 이렇게 하면 Dto (나 원하는 클래스) 로 반환 가능.
    @Query("select new spring.datajpa.dto.MemberDto(m.id, m.username, t.name)" +
            " from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 컬렉션 파라미터 바인딩. collection 타입으로 in절 지원
    // in절로 여러개를 조회하고 싶을 때 사용
    // in 절을 깔끔하게 선언할 수 있음
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    // 아래와 같은 다양한 반환타입 제공
    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalMemberByUsername(String username);

}

