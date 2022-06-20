package spring.datajpa.repository;

import org.springframework.stereotype.Repository;
import spring.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

// 순수 jpa 레퍼지토리. 데이터 jpa 사용하지 않
@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    // NullPointerException을 방지하기 위한 Optional
    // Null이 될 수 있는 값을 감싸는 wrapper class
    // ofNullable()은 null이 올 수도 있고 아닐 수도 있는 경우에 사용.
    // member가 null일 경우 Npe가 아닌 빈 객체를 던
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count(){
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    // 회원의 이름과 나이를 기준으로 만듦
    public List<Member> findByUsernameAndAgeGreaterThen(String usernaem, int age) {
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", usernaem)
                .setParameter("age", age)
                .getResultList();

    }

    // 페이징
    public List<Member> findByPaging(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
                .setParameter("age", age)
                .setFirstResult(offset) //어디서부터 가져올지
                .setMaxResults(limit) // 몇 개를 가져올지
                .getResultList();

    }

    // 전체 수 받아오는 카운트
    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    // 벌크성 수정: 전체 회원의 나이를 하나 증가시키는 예제
    public int bulkAgePlus(int age) {
        return em.createQuery("update Member m set m.age = m.age + 1" +
                        " where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate(); // 응답 값의 개수가 나온다
    }
}
