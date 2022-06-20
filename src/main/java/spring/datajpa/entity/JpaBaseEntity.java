package spring.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class JpaBaseEntity {

    @Column(updatable = false) // createDate는 실수로라도 변경되지 못하게.
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @PrePersist // persist 하기 전에 이벤트가 발생하는 것
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createDate = now;
        updateDate = now; // 등록/수정일을 맞춰놓는게 관리상 좋다. 쿼리할때도 null보단 좋다.
    }

    @PreUpdate // update 전에 호출
    public void preUpdate(){
        updateDate = LocalDateTime.now();
    }

}
