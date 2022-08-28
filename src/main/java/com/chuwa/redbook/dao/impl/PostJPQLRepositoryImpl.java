package com.chuwa.redbook.dao.impl;

import com.chuwa.redbook.dao.PostJPQLRepository;
import com.chuwa.redbook.entity.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author b1go
 * @date 8/27/22 6:19 PM
 */
@Repository
public class PostJPQLRepositoryImpl implements PostJPQLRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Post> getAllPostWithJPQL() {
        TypedQuery<Post> posts = entityManager.createNamedQuery("get_all_posts", Post.class);
        return posts.getResultList();
    }
}
