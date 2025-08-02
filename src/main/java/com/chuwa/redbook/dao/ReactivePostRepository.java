package com.chuwa.redbook.dao;

import com.chuwa.redbook.entity.Post;
import org.springframework.data.r2dbc.repository.Query; // Use r2dbc library instead of JPA/hibernate
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReactivePostRepository extends ReactiveCrudRepository<Post, Long> {

    @Query("SELECT * FROM posts LIMIT 1")
    Mono<Post> findFirstPost();
}