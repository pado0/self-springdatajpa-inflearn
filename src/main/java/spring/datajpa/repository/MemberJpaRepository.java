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
}
