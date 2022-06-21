package spring.datajpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class Item implements Persistable<String> {

    @Id
    private String id;

    @CreatedDate // 이것도 jpa 이벤트
    private LocalDateTime createdDate;

    // 새것을 판단하는 메소드를 직접 만들어야함.
    @Override
    public boolean isNew() {
        return createdDate == null; // createdDate에 값이 없으면 true
    }
}
