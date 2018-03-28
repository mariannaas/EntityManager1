package com.devskiller.dao;

import com.devskiller.model.Item;
import com.devskiller.model.Review;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Transactional
public class ItemRepository {

    @PersistenceContext
    EntityManager entityManager;

    public Page<Item> findItems(PageRequest pageRequest) {
        List<Item> content = new ArrayList<>();
        List<Object> resultlist =  getAllElementsInEntityTable(Item.class);
        for (int i = 0; i < pageRequest.getCount();i++){
            Item item = (Item) resultlist.get(i);
            content.add(item);

        }
       return new Page<Item>(content, pageRequest.getPageNumber(), pageRequest.getCount());
    }


    public List<Item> findItemsWithAverageRatingLowerThan(Integer rating) {
        List<Item> returnItemList = new ArrayList<>();
        List<Object> resultlist = getAllElementsInEntityTable(Item.class);
        for(Object o: resultlist) {
            Item item = (Item) o;
           if ( calculateAverageReviewsRatingOfItem(item) < rating)
               returnItemList.add(item);
        }
        return returnItemList;
    }

    private double calculateAverageReviewsRatingOfItem(Item item) {
        Set<Review> reviewSet = item.getReviews();
        double avgRating = 0;
        for(Review review : reviewSet){
            avgRating+=review.getRating();
        }
        return avgRating / reviewSet.size();
    }

    private List<Object> getAllElementsInEntityTable(Class entityClass) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<Item> from = criteriaQuery.from(entityClass);
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        TypedQuery<Object> typedQuery= entityManager.createQuery(select);
        return typedQuery.getResultList();
    }

}
