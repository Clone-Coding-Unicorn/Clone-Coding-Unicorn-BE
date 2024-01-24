package com.sparta.clonecodingunicorn.domain.post.dto;

import com.sparta.clonecodingunicorn.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PostResponseDto {
    private Long postId;
    private String title;
    private String contents;
    private LocalDate date;
    private String imageUrl;
    private String category;

    public PostResponseDto(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.date = post.getDate();
        this.imageUrl = post.getImageUrl();
        this.category = post.getCategory();

    }
}


