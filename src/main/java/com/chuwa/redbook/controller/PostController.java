package com.chuwa.redbook.controller;

import com.chuwa.redbook.payload.PostDto;
import com.chuwa.redbook.payload.PostResponse;
import com.chuwa.redbook.service.PostService;
import com.chuwa.redbook.service.impl.PostServiceImpl;
import com.chuwa.redbook.util.AppConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.chuwa.redbook.exception.*;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    Logger logger = org.slf4j.LoggerFactory.getLogger(PostController.class);

    @GetMapping("/readiness")
    public Mono<PostDto> isHealthy() {
        return (Mono)(postService.isReady());
    }

    @GetMapping("/liveness")
    public Mono<String> isLive() {
        return postService.isLive();
    }

    @PostMapping
    public Mono<ResponseEntity<PostDto>> createPost(@RequestBody PostDto postDto) {
        return postService.createPost(postDto)
                .map(savedPost -> ResponseEntity.status(HttpStatus.CREATED).body(savedPost));
    }

    @GetMapping()
    public Mono<PostResponse> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir
    ) {
        logger.info("pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}", pageNo, pageSize, sortBy, sortDir);
        return postService.getAllPost(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public Mono<PostDto> getPostById(@PathVariable(name = "id") long id) {
        logger.info("id: {}", id);
        return postService.getPostById(id);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<PostDto>> updatePostById(
            @RequestBody PostDto postDto,
            @PathVariable(name = "id") long id) {

        logger.info("id: {}", id);

        return postService.updatePost(postDto, id)
                .map(updatedPost -> ResponseEntity.ok(updatedPost))// 200
                .onErrorResume(ResourceNotFoundException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build())) //404
                .onErrorResume(ServerInternalException.class,
                        ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())); // 500
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deletePost(@PathVariable(name = "id") long id) {
        logger.info("id: {}", id);

        return postService.deletePostById(id)
                .then(Mono.just(ResponseEntity.ok("Post entity deleted successfully.")))
                .onErrorResume(ResourceNotFoundException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build()));
    }
}
