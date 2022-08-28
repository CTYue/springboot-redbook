package com.chuwa.redbook.dao.impl;

import com.chuwa.redbook.dao.PostJPQLRepository;
import com.chuwa.redbook.entity.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author b1go
 * @date 8/27/22 6:19 PM
 */
@Repository
@Transactional
public class PostJPQLRepositoryImpl implements PostJPQLRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Post> getAllPostWithJPQL() {
        TypedQuery<Post> posts = entityManager.createNamedQuery("get_all_posts", Post.class);
        return posts.getResultList();
    }

    /**
     * entityManager:
     *  1. Insert Update: entityManager.merge()
     *  2. Select: entityManager.find();
     *  3. delete: entityManager.remove();
     *
     *  代码的测试在test package下
     */

    public Post insertPost(Post post) {
        // post中可以無ID
        return entityManager.merge(post);
    }

    public Post updatePost(Post post) {
        // post中必须有id才行。
        return entityManager.merge(post);
    }

    public Post getPostById(Long id) {
        return entityManager.find(Post.class, id);
    }

    public void deleteById(Long id) {
        Post post = entityManager.find(Post.class, id);
        entityManager.remove(post);
    }

    /**
     * 练习下generic
     * @param t
     * @return
     * @param <T>
     */
    public <T> T insertData(T t) {
        return entityManager.merge(t);
    }
}
