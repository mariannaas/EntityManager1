package com.devskiller.dao;

import com.devskiller.context.PersistenceConfiguration;
import com.devskiller.model.Item;
import com.devskiller.model.Review;
import com.devskiller.model.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfiguration.class)
@Transactional
public class ItemRepositoryTest {

  @PersistenceContext
  EntityManager em;

  @Autowired
  ItemRepository itemRepository;

  @org.junit.Test
  public void shouldReturnFirstPageOfItems() {
    Page<Item> items = itemRepository.findItems(new PageRequest(0, 10));
    assertThat("There should be 10 items on first page", items.getContent().size(), equalTo(10));
  }

  @Test
  public void shouldReturnItemsWithAverageRating() {
    assertThat("There should be 10 items with avg rating < 10",
        itemRepository.findItemsWithAverageRatingLowerThan(10).size(), equalTo(10));
    assertThat("There should be 1 items with avg rating < 1",
        itemRepository.findItemsWithAverageRatingLowerThan(1).size(), equalTo(1));
  }

  @Before
  public void prepareData() {
    User user = new User(RandomString.nextString(5));
    em.persist(user);
    for (int i=0; i<15; i++) {
      Item item = new Item("title " + i, "description");
      item.addReview(new Review( i < 10 ? i : 10, "Review "+i+"/1", user));
      item.addReview(new Review( i < 10 ? i+1 : 10, "Review "+i+"/2", user));
      em.persist(item);
    }
  }


}
