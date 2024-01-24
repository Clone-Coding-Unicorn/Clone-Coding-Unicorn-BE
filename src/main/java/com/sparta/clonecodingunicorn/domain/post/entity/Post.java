package com.sparta.clonecodingunicorn.domain.post.entity;

import com.sparta.clonecodingunicorn.domain.post.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String contents;

    @Column
    private LocalDate date;

    @Column
    private String imageUrl;

    @Column
    private String category;



    public Post(String newsTitle, String contents, String firstImageUrl, LocalDate newsDate, String dbCategory) {
        this.title=newsTitle;
        this.contents=contents;
        this.date=newsDate;
        this.imageUrl=firstImageUrl;
        this.category=dbCategory;
    }


    public Post updatePost(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.date = requestDto.getDate();
        this.imageUrl = requestDto.getImageUrl();
        this.category = requestDto.getCategory();
        return this;
    }
}



