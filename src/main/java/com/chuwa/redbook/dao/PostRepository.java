package com.chuwa.redbook.dao;

import com.chuwa.redbook.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author b1go
 * @date 8/22/22 6:48 PM
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List findByTitle(String title);
    // No need to write code
    List findByTitleAndDescription(String title, String description);
    List getByTitleAndDescription(String title, String description);
}
