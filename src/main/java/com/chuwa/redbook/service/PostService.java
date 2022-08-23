package com.chuwa.redbook.service;

import com.chuwa.redbook.payload.PostDto;

/**
 * @author b1go
 * @date 8/22/22 6:51 PM
 */
public interface PostService {

    PostDto createPost(PostDto postDto);
}
