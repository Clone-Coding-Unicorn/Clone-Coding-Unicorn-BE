package com.sparta.clonecodingunicorn.domain.post.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PostRequestDto {
    private String title;
    private String contents;
    private LocalDate date;
    private String imageUrl;
    private String category;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
    public void setDate(LocalDate date) {
        this.date=date;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}


