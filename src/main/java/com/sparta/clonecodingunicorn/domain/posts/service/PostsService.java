package com.sparta.clonecodingunicorn.domain.posts.service;

import com.sparta.clonecodingunicorn.domain.posts.dto.PostsDetailsResponseDto;
import com.sparta.clonecodingunicorn.domain.posts.dto.PostsResponseDto;
import com.sparta.clonecodingunicorn.domain.posts.entity.Posts;
import com.sparta.clonecodingunicorn.domain.posts.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
@Slf4j
public class PostsService {
    private final PostsRepository postsRepository;

    public List<Object> getPosts(int page, int size,
                                 String sortBy, boolean isAsc) {
        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy, "title");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Posts> newsList = postsRepository.findAll(pageable);

        List<PostsResponseDto> postsResponseDtoList = newsList.stream()
                .map(PostsResponseDto::new)
                .collect(Collectors.toList());

        List<Object> response = new ArrayList<>();
        response.add(newsList.getTotalPages());
        response.add(postsResponseDtoList);

        return response;
    }

    public List<Object> getPostsByCategory(String category, int page, int size, String sortBy, boolean isAsc) {
        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy, "title");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Posts> newsListByCategory = postsRepository.findAllByCategory(category, pageable);

        if (newsListByCategory.getContent().isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");
        }

        List<PostsResponseDto> postsResponseDto = newsListByCategory.stream()
                .map(PostsResponseDto::new)
                .collect(Collectors.toList());

        List<Object> response = new ArrayList<>();
        response.add(newsListByCategory.getTotalPages());
        response.add(postsResponseDto);

        return response;
    }


    public PostsDetailsResponseDto getPostsDetails(Long postId) {

        Posts posts = postsRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 뉴스 입니다.")
        );
        return new PostsDetailsResponseDto(posts);
    }
}