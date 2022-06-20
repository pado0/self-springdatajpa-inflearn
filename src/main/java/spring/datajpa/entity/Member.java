package spring.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
public class Member extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this(username, 0);
    }


    public Member(String username, int age) {
        this(username, age, null);
    }


    // 팀 세팅시 연관관계 편의 메소들르 통해 팀 객체쪽도 변경
    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    // 연관관계 편의 메소드
    private void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this); // team의 컬렉션에 선택된 멤버객체를 add
    }
}
