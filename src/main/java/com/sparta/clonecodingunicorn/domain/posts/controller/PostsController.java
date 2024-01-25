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

    //
    @GetMapping() // 전체 뉴스 조회
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

    @GetMapping("/category") // 카테고리별 뉴스 조회
    public ResponseEntity<List<Object>> getPostsByCategory(
            @RequestParam("category") String category,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
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


    @GetMapping("/{postId}") // 뉴스 상세페이지 조회
    public ResponseEntity<PostsDetailsResponseDto> getPostsDetails(@PathVariable Long postId) {
        PostsDetailsResponseDto postsDetails = postsService.getPostsDetails(postId);

        return ResponseEntity.ok(postsDetails);
    }
}