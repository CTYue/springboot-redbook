package com.chuwa.redbook.service;

import com.chuwa.redbook.payload.PostDto;
import com.chuwa.redbook.payload.PostResponse;
import reactor.core.publisher.Mono;


public interface PostService {


    Mono<PostResponse> getAllPost(int pageNo, int pageSize, String sortBy, String sortDir);

    Mono<PostDto> createPost(PostDto postDto);

    Mono<PostDto> getPostById(long id);

    Mono<Void> deletePostById(long id);

    Mono<PostDto> isReady();

    Mono<PostDto> updatePost(PostDto postDto, long id);

    Mono<String> isLive();
}