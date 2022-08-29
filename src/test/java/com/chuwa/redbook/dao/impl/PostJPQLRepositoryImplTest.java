package com.chuwa.redbook.dao.impl;

import com.chuwa.redbook.entity.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.provider.HibernateUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author b1go
 * @date 8/28/22 12:06 PM
 */
@SpringBootTest
class PostJPQLRepositoryImplTest {
    @Autowired
    private PostJPQLRepositoryImpl repository;

    private Post post = new Post(null, "title test", "decription test", "content test",
            null, null);

    @Test
    void getAllPostWithJPQL() {
        List<Post> posts = repository.getAllPostWithJPQL();
        System.out.println(posts);
    }

    @Test
    void insertPost() {
        // 防止title 重复
        post.setTitle(post.getTitle() + LocalDateTime.now());
        Post savedPost = repository.insertPost(post);
        System.out.println(savedPost);
    }

    @Test
    void updatePost() {
        // update we should provide the id
        post.setId(12L);
        post.setTitle(post.getTitle() + LocalDateTime.now());
        Post savedPost = repository.insertPost(post);
        System.out.println(savedPost);
    }

    @Test
    void getPostById() {
        Post postById = repository.getPostById(12L);
        System.out.println(postById);
    }

    @Test
    void deleteById() {
        post.setTitle(post.getTitle() + LocalDateTime.now());
        Post savedPost = repository.insertPost(post);
        System.out.println(savedPost);
        Long id = savedPost.getId();
        repository.deleteById(id);
        Post postById = repository.getPostById(id);
        System.out.println(postById);
    }

    @Test
    void insertData() {
        post.setTitle(post.getTitle() + LocalDateTime.now());
        Post savedPost = repository.insertData(post);
        System.out.println(savedPost);
    }

    /**
     * learn SessionFactory
     * 注意要在Post entity 的date那里加    @Column(name = "update_date_time")
     */
    @Test
    void testSessionFactory() {
        // 1. 根据配置文件，创建一个sessionFactory
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        // 2. sessionFactory 创建出一个session
        Session session = sessionFactory.openSession();

        Transaction transaction = null;
        try {
            // 3. session 开始一个transaction
            transaction = session.beginTransaction();

            // 4. 执行txn
            post.setTitle(post.getTitle() + LocalDateTime.now());
            post.setCreateDateTime(LocalDateTime.now());
            post.setUpdateDateTime(LocalDateTime.now());
            Post savedPost = (Post) session.merge(post);
            System.out.println(savedPost);

            // 5. commit txn, 该txn要么成功，要么失败,是个原子操作。
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                // txn 失败则回滚
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // 6. close session
            session.close();
        }
    }
}