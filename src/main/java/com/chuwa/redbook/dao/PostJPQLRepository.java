package com.chuwa.redbook.dao;

import com.chuwa.redbook.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author b1go
 * @date 8/22/22 6:48 PM
 */
@Repository
public interface PostJPQLRepository {

    List<Post> getAllPostWithJPQL();
}
