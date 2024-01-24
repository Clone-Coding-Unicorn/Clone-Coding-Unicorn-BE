package com.sparta.clonecodingunicorn.domain.posts.controller;

import com.sparta.clonecodingunicorn.domain.posts.dto.PostsDetailsResponseDto;
import com.sparta.clonecodingunicorn.domain.posts.service.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostsController {

    private final PostsService postsService;

    @GetMapping()
    public ResponseEntity<List<Object>> getPosts(@RequestParam(name = "page", defaultValue = "1") int page,
                                                 @RequestParam(name = "size", defaultValue = "12") int size,
                                                 @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                                 @RequestParam(name = "isAsc", defaultValue = "true") boolean isAsc) {

        List<Object> postsResponseDtoList = postsService.getPosts(
                page-1,
                size,
                sortBy,
                isAsc
        );

        return ResponseEntity.ok(postsResponseDtoList);
    }

    @GetMapping("/category")
    public ResponseEntity<List<Object>> getPostsByCategory(
            @RequestParam("category") String category,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "12") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
            @RequestParam(name = "isAsc", defaultValue = "true") boolean isAsc) {

        List<Object> postsResponseDtoList = postsService.getPostsByCategory(
                category,
                page-1,
                size,
                sortBy,
                isAsc
        );

        return ResponseEntity.ok(postsResponseDtoList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Object>> searchPosts(
            @RequestParam("keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "12") int size) {

        List<Object> postsResponseDtoList = postsService.searchPosts(
                keyword,
                page-1,
                size
        );

        return ResponseEntity.ok(postsResponseDtoList);
    }

    @GetMapping("/search/basic")
    public ResponseEntity<List<Object>> searchPostsBasic(
            @RequestParam("keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "12") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
            @RequestParam(name = "isAsc", defaultValue = "true") boolean isAsc) {

        List<Object> postsResponseDtoList = postsService.searchPostsBasic(
                keyword,
                page-1,
                size,
                sortBy,
                isAsc
        );

        return ResponseEntity.ok(postsResponseDtoList);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostsDetailsResponseDto> getPostsDetails(@PathVariable Long postId) {
        PostsDetailsResponseDto postsDetails = postsService.getPostsDetails(postId);

        return ResponseEntity.ok(postsDetails);
    }
}