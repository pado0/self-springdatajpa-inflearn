package spring.datajpa.dto;

import lombok.Data;
import spring.datajpa.entity.Member;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    // dto에서는 엔티티 참조 가능
    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
