package com.chuwa.redbook.dao;

import com.chuwa.redbook.entity.Post;
import org.springframework.data.r2dbc.repository.Query; // ✅ Correct R2DBC Query annotation
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono; // Use Mono for a single result

@Repository
public interface PostRepository extends ReactiveCrudRepository<Post, Long> {

    @Query("SELECT * FROM posts LIMIT 1") // ✅ Match your table name (posts)
    Mono<Post> findFirstPost();
}