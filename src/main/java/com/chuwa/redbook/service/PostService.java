package com.chuwa.redbook.service;

import com.chuwa.redbook.payload.PostDto;
import com.chuwa.redbook.payload.PostResponse;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author b1go
 * @date 8/22/22 6:51 PM
 */
public interface PostService<T> {

//    PostDto createPost(PostDto postDto);

//    Flux<T> getAllPost();

//    /**
//     * 分页
//     * @param pageNo
//     * @param pageSize
//     * @param sortBy
//     * @param sortDir
//     * @return
//     */
//    PostResponse getAllPost(int pageNo, int pageSize, String sortBy, String sortDir);

//    PostDto getPostById(long id);
//
//    PostDto updatePost(PostDto postDto, long id);
//
//    void deletePostById(long id);

    T isReady();
}