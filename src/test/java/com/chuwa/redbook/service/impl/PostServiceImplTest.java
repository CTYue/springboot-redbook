package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.RedbookApplication;
import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.PostDto;
import com.chuwa.redbook.payload.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author b1go
 * @date 9/16/22 12:31 AM
 */
@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImplTest.class);

    /**
     * 创建一个Mock对象
     * 也可以通过PostRepository postRepository = Mockito.mock(PostRepository.class)
     * 区别是， @Mock这种方式会被@InjectMocks识别，并注入给该变量
      */
    @Mock
    private PostRepository postRepositoryMock;

    /**
     * 未定义的方法（behavior），则调用真实的方法。
     * 已定义的方法when().thenReturn(), 则调用mock的虚假behavior。
     */
//    @Spy
//    private ModelMapper modelMapper;

    @Mock(name="modelMapper")
    private ModelMapper mockedModelMapper;

    /**
     * 为该class依赖的变量，注入对应的Mock对象。
     * 比如PostServiceImpl依赖postRepository 和modelMapper,则将上面@Mock和@Spy修饰的注入进来
     */
    @InjectMocks
    private PostServiceImpl postService;

    /**
     * 定义两个fields
     */
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
        this.postDto = new PostDto();
        postDto.setId(1L);
        postDto.setTitle("xiao ruishi");
        postDto.setContent("wanqu xiao ruishi");
        postDto.setDescription("wanqu");
    }

    @Test
    public void testCreatePostWithMockedModelMapper() {
        // Define modelMapper's two different behaviors (different converting sources and targets)
        Mockito.when(mockedModelMapper.map(ArgumentMatchers.any(PostDto.class), ArgumentMatchers.eq(Post.class))).thenReturn(post);
        Mockito.when(mockedModelMapper.map(ArgumentMatchers.any(Post.class), ArgumentMatchers.eq(PostDto.class))).thenReturn(postDto);
        Mockito.when(postRepositoryMock.save(ArgumentMatchers.any())).thenReturn(post);
        PostDto postResponse = postService.createPost(postDto);

        // assertions
        Assertions.assertEquals(postDto.getTitle(), postResponse.getTitle());
        Assertions.assertEquals(postDto.getDescription(), postResponse.getDescription());
        Assertions.assertEquals(postDto.getContent(), postResponse.getContent());
    }

//    @Test
//    public void testCreatePostWithSpiedModelMapper() {
//        modelMapper = new ModelMapper();
//
//        // Although real modelMapper is invoked, behaviors of postRepository still needs to be defined as postRepository is a mocked object
//        // Mocked objects have skeletons only.
//        Mockito.when(postRepositoryMock.save(ArgumentMatchers.any())).thenReturn(post);
//
//        PostDto postResponse = postService.createPost(postDto);
//
//        Assertions.assertEquals(postDto.getTitle(), postResponse.getTitle());
//        Assertions.assertEquals(postDto.getDescription(), postResponse.getDescription());
//        Assertions.assertEquals(postDto.getContent(), postResponse.getContent());
//    }

    @Test
    public void testGetAllPost() {
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        // define the behaviors
        Mockito.when(mockedModelMapper.map(ArgumentMatchers.any(Post.class), ArgumentMatchers.eq(PostDto.class))).thenReturn(postDto);

        Mockito.when(postRepositoryMock.findAll())
                .thenReturn(posts);

        // execute
        List<PostDto> postDtos = postService.getAllPost();

        // assertions
        Assertions.assertNotNull(postDtos);
        Assertions.assertEquals(1, postDtos.size());
        PostDto postResponse = postDtos.get(0);
        Assertions.assertEquals(postDto.getTitle(), postResponse.getTitle());
        Assertions.assertEquals(postDto.getDescription(), postResponse.getDescription());
        Assertions.assertEquals(postDto.getContent(), postResponse.getContent());
    }

    @Test
    public void testGetPostById() {
        // define the behaviors
        Mockito.when(postRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(post));
        Mockito.when(mockedModelMapper.map(ArgumentMatchers.any(Post.class), ArgumentMatchers.eq(PostDto.class))).thenReturn(postDto);

        // execute
        PostDto postResponse = postService.getPostById(1L);

        // assertions
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(postDto.getTitle(), postResponse.getTitle());
        Assertions.assertEquals(postDto.getDescription(), postResponse.getDescription());
        Assertions.assertEquals(postDto.getContent(), postResponse.getContent());
    }

    @Test
    public void testGetPostById_ResourceNotFoundException() {
        // define the behaviors
        Mockito.when(postRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new ResourceNotFoundException("Post", "id", 1L));

        // execute and assertions
        Assertions.assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(1L));
    }

    @Test
    public void testUpdatePost() {
        String description = "UPDATED - " + post.getDescription();
        postDto.setDescription(description);

        // deep copy
        Post updatedPost = new Post();
        updatedPost.setId(post.getId());
        updatedPost.setTitle(post.getTitle());
        updatedPost.setDescription(description);
        updatedPost.setContent(post.getContent());

        // define the behaviors
        Mockito.when(postRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(post));

        Mockito.when(postRepositoryMock.save(ArgumentMatchers.any(Post.class)))
                .thenReturn(updatedPost);

        Mockito.when(mockedModelMapper.map(ArgumentMatchers.any(Post.class), ArgumentMatchers.eq(PostDto.class))).thenReturn(postDto);
        // execute
        PostDto postResponse = postService.updatePost(postDto, 1L);

        // assertions
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(postDto.getTitle(), postResponse.getTitle());
        Assertions.assertEquals(postDto.getDescription(), postResponse.getDescription());
        Assertions.assertEquals(postDto.getContent(), postResponse.getContent());
    }

    @Test
    public void testUpdatePost_ResourceNotFoundException() {
        // define the behaviors
        Mockito.when(postRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new ResourceNotFoundException("Post", "id", 1L));

        // execute and assertions
        Assertions.assertThrows(ResourceNotFoundException.class, () -> postService.updatePost(postDto,1L));
    }

    @Test
    public void testDeletePostById() {
        // define the behaviors
        Mockito.when(postRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(post));
        // postRepositoryMock.delete() is a void method
        Mockito.doNothing().when(postRepositoryMock).delete(ArgumentMatchers.any(Post.class));

        // execute
        postService.deletePostById(1L);

        // verify
        // 验证 postRepositoryMock.delete() 被执行过一次
        Mockito.verify(postRepositoryMock, Mockito.times(1)).delete(ArgumentMatchers.any(Post.class));
    }

    @Test
    public void testDeletePostById_ResourceNotFoundException() {
        // define the behaviors
        Mockito.when(postRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new ResourceNotFoundException("Post", "id", 1L));

        // execute and assertions
        Assertions.assertThrows(ResourceNotFoundException.class, () -> postService.deletePostById(1L));
    }

    @Test
    public void testGetAllPost_Pageable() {
        int pageNo = 1;
        int pageSize = 8;
        String sortBy = "title";
        String sortDir = Sort.Direction.ASC.name();
        Long totalElements = 22L;
        int totalPages = (int) Math.ceil(totalElements/pageSize);
        boolean isLast = pageNo == totalPages;

        List<Post> posts = new ArrayList<>();
        posts.add(post);

        // define the behaviors
        Page<Post> pagePosts = Mockito.mock(Page.class);
        Mockito.when(pagePosts.getContent()).thenReturn(posts);
        Mockito.when(pagePosts.getNumber()).thenReturn(pageNo);
        Mockito.when(pagePosts.getSize()).thenReturn(pageSize);
        Mockito.when(pagePosts.getTotalElements()).thenReturn(totalElements);
        Mockito.when(pagePosts.getTotalPages()).thenReturn(totalPages);
        Mockito.when(pagePosts.isLast()).thenReturn(isLast);

        Mockito.when(mockedModelMapper.map(ArgumentMatchers.any(Post.class), ArgumentMatchers.eq(PostDto.class))).thenReturn(postDto);
        Mockito.when(postRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(pagePosts);

        // execute
        PostResponse postResponse = postService.getAllPost(pageNo, pageSize, sortBy, sortDir);

        // verify
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(pageNo, postResponse.getPageNo());
        Assertions.assertEquals(pageSize, postResponse.getPageSize());
        Assertions.assertEquals(totalElements, postResponse.getTotalElements());
        Assertions.assertEquals(totalPages, postResponse.getTotalPages());
        Assertions.assertEquals(isLast, postResponse.isLast());

        List<PostDto> content = postResponse.getContent();
        Assertions.assertNotNull(content);
        Assertions.assertEquals(posts.size(), content.size());
        PostDto dto = content.get(0);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(postDto.getTitle(), dto.getTitle());
        Assertions.assertEquals(postDto.getDescription(), dto.getDescription());
        Assertions.assertEquals(postDto.getContent(), dto.getContent());
    }

    @Test
    public void testGetAllPost_Pageable_SortDirDescending() {
        int pageNo = 1;
        int pageSize = 8;
        String sortBy = "title";
        String sortDir = Sort.Direction.DESC.name();
        Long totalElements = 22L;
        int totalPages = (int) Math.ceil(totalElements/pageSize);
        boolean isLast = pageNo == totalPages;

        List<Post> posts = new ArrayList<>();
        posts.add(post);

        // define the behaviors
        Page<Post> pagePosts = Mockito.mock(Page.class);
        Mockito.when(pagePosts.getContent()).thenReturn(posts);
        Mockito.when(pagePosts.getNumber()).thenReturn(pageNo);
        Mockito.when(pagePosts.getSize()).thenReturn(pageSize);
        Mockito.when(pagePosts.getTotalElements()).thenReturn(totalElements);
        Mockito.when(pagePosts.getTotalPages()).thenReturn(totalPages);
        Mockito.when(pagePosts.isLast()).thenReturn(isLast);

        Mockito.when(postRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(pagePosts);
        Mockito.when(mockedModelMapper.map(ArgumentMatchers.any(Post.class), ArgumentMatchers.eq(PostDto.class))).thenReturn(postDto);

        // execute
        PostResponse postResponse = postService.getAllPost(pageNo, pageSize, sortBy, sortDir);

        // verify
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(pageNo, postResponse.getPageNo());
        Assertions.assertEquals(pageSize, postResponse.getPageSize());
        Assertions.assertEquals(totalElements, postResponse.getTotalElements());
        Assertions.assertEquals(totalPages, postResponse.getTotalPages());
        Assertions.assertEquals(isLast, postResponse.isLast());

        List<PostDto> content = postResponse.getContent();
        Assertions.assertNotNull(content);
        Assertions.assertEquals(posts.size(), content.size());
        PostDto dto = content.get(0);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(postDto.getTitle(), dto.getTitle());
        Assertions.assertEquals(postDto.getDescription(), dto.getDescription());
        Assertions.assertEquals(postDto.getContent(), dto.getContent());
    }
}