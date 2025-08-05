package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.ReactivePostRepository;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.exception.ServerInternalException;
import com.chuwa.redbook.payload.PostDto;
import com.chuwa.redbook.payload.PostResponse;
import com.chuwa.redbook.service.PostService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private ReactivePostRepository reactivePostRepository;

    private final WebClient webClient;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    public PostServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<PostDto> isReady() {
        return reactivePostRepository.findAll().next().map(this::mapToDTO);
    }

    public Mono<String> isLive() {
        return webClient.get()
                .uri("/citizen/test")   // path relative to baseUrl
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(resp -> logger.info("Response: " + resp))
                .doOnError(err -> logger.error("Error: " + err.getMessage()));
    }


    @Override
    public Mono<PostDto> createPost(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle().isEmpty() ? "No Title" : postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        // covert DTO to Entity
        post = mapToEntity(postDto);

        return reactivePostRepository.save(post).map(this::mapToDTO);
    }


    @Override
    public Mono<PostDto> getPostById(long id) {
        return reactivePostRepository.findById(id)
                .map(post -> mapToDTO(post))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Post", "id", id)));
    }

    @Override
    public Mono<PostDto> updatePost(PostDto postDto, long id) {
        return reactivePostRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Post", "id", id)))
                .flatMap(post -> {
                    post.setTitle(postDto.getTitle());
                    post.setDescription(postDto.getDescription());
                    post.setContent(postDto.getContent());
                    return reactivePostRepository.save(post);
                })
                .onErrorMap(ex -> new ServerInternalException("Failed to update post: " + id))
                .map(this::mapToDTO);
    }

    @Override
    public Mono<Void> deletePostById(long id) {
        return reactivePostRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Post", "id", id)))
                .flatMap(post -> reactivePostRepository.delete(post));
    }


    @Override
    public Mono<PostResponse> getAllPost(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Pagination
        return reactivePostRepository.count()
                .flatMap(totalElements -> reactivePostRepository.findAll()
                        .skip((long) pageNo * pageSize)
                        .take(pageSize) // Take the current page
                        .map(this::mapToDTO)
                        .collectList()
                        .map(postDtos -> {
                            PostResponse postResponse = new PostResponse();
                            postResponse.setContent(postDtos);
                            postResponse.setPageNo(pageNo);
                            postResponse.setPageSize(pageSize);
                            postResponse.setTotalElements(totalElements);
                            postResponse.setTotalPages((int) Math.ceil((double) totalElements / pageSize));
                            postResponse.setLast(pageNo + 1 >= postResponse.getTotalPages());
                            return postResponse;
                        })
                );
    }

    private PostDto mapToDTO(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());

        return postDto;
    }

    private Post mapToEntity(PostDto postDto){
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        return post;
    }
}
