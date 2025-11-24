package com.chuwa.redbook.tools;

import com.chuwa.redbook.payload.PostDto;
import com.chuwa.redbook.service.PostService;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MCP Tools for Post content management
 * Provides AI agents with tools to create, lookup, update, and delete posts
 */
@Component
public class ContentTools {

    private final PostService postService;

    public ContentTools(PostService postService) {
        this.postService = postService;
    }

    @McpTool(name = "createPost", description = "Create a new post in the Redbook application with title, description, and content")
    public PostDto createPost(
            @McpToolParam(description = "Post title", required = true) String title,
            @McpToolParam(description = "Post description", required = true) String description,
            @McpToolParam(description = "Post content", required = true) String content) {
        PostDto postDto = new PostDto();
        postDto.setTitle(title);
        postDto.setDescription(description);
        postDto.setContent(content);
        return postService.createPost(postDto);
    }

    @McpTool(name = "getPostById", description = "Lookup and retrieve a specific post by its ID")
    public PostDto getPostById(
            @McpToolParam(description = "Post ID to lookup", required = true) long id) {
        return postService.getPostById(id);
    }

    @McpTool(name = "getAllPosts", description = "Get all posts as a list")
    public List<PostDto> getAllPosts() {
        return postService.getAllPost();
    }

    @McpTool(name = "updatePost", description = "Update an existing post by ID with new title, description, and content")
    public PostDto updatePost(
            @McpToolParam(description = "Post ID to update", required = true) long id,
            @McpToolParam(description = "New post title", required = true) String title,
            @McpToolParam(description = "New post description", required = true) String description,
            @McpToolParam(description = "New post content", required = true) String content) {
        PostDto postDto = new PostDto();
        postDto.setTitle(title);
        postDto.setDescription(description);
        postDto.setContent(content);
        return postService.updatePost(postDto, id);
    }

    @McpTool(name = "deletePost", description = "Delete a post by its ID permanently from the system")
    public Map<String, String> deletePost(
            @McpToolParam(description = "Post ID to delete", required = true) long id) {
        postService.deletePostById(id);
        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "Post with ID " + id + " has been deleted successfully");
        return result;
    }

    @McpTool(name = "checkReadiness", description = "Check if the Redbook application is ready and healthy")
    public Map<String, String> checkReadiness() {
        PostDto readyPost = postService.isReady();
        Map<String, String> result = new HashMap<>();
        result.put("status", "ready");
        result.put("samplePostId", readyPost != null ? String.valueOf(readyPost.getId()) : "none");
        return result;
    }
}
