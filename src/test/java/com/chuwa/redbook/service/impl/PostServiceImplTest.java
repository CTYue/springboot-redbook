package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.payload.PostDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;


/**
 * @author b1go
 * @date 9/16/22 12:31 AM
 */
@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImplTest.class);

    @Mock
    private PostRepository postRepository;

    /**
     * 未定义的方法（behavior），则调用真实的方法。
     * 已定义的方法when().thenReturn(), 则调用mock的虚假behavior。
     */
    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private PostServiceImpl postService;

    private PostDto postDto;
    private Post post;


    @BeforeAll
    static void beforeAll() {
        logger.info("START test");
    }

    @BeforeEach
    void setUp() {
        logger.info("set up Post for each test");

        this.post = new Post(1L, "xiao ruishi", "wanqu", "wanqu xiao ruishi",
                LocalDateTime.now(), LocalDateTime.now());
        ModelMapper modelMapper = new ModelMapper();
        this.postDto = modelMapper.map(this.post, PostDto.class);
    }

    @Test
    public void testCreatePost() {
        // define the behaviors
        Mockito.when(postRepository.save(ArgumentMatchers.any(Post.class)))
                .thenReturn(post);

        // execute
        PostDto postResponse = postService.createPost(postDto);

        // assertions
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(postDto.getTitle(), postResponse.getTitle());
        Assertions.assertEquals(postDto.getDescription(), postResponse.getDescription());
        Assertions.assertEquals(postDto.getContent(), postResponse.getContent());
    }

}