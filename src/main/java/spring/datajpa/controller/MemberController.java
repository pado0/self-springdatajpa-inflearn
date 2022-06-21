package spring.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring.datajpa.dto.MemberDto;
import spring.datajpa.entity.Member;
import spring.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    // 도메인 클래스 컨버터 적용
    // 스프링이 중간에서 컨버팅을 자동으로 해준다.
    // 권장하지는 않는 기능. pk로 외부에 공개해서 조회하는 경우가 흔치는 않다.
    // 간단할때만 사용할 수 있다.
    // 레포지토리를 사용해서 엔티티를 찾는 것이다.
    // 트랜잭션 범위가 없는 상황에서 조회해서 조회만 하는게 좋다.
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(Pageable pageable){ //page: 결과정보, pageable: 파라미터 정보
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> map = page.map(MemberDto::new);
        return map;
    }

    //@PostConstruct // 스프링 앱이 올라올 때 실행됨
    public void init(){
        memberRepository.save(new Member("userA"));
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }


}
