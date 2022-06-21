package spring.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.datajpa.entity.Item;
import spring.datajpa.repository.ItemRepository;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void save(){
        Item item = new Item();
        itemRepository.save(item); // persist가 되지 않아 pk가 null인 상태에서 save
    }
}
