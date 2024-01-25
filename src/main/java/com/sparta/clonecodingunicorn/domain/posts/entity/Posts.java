package com.sparta.clonecodingunicorn.domain.posts.entity;

import com.sparta.clonecodingunicorn.domain.posts.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Posts extends Timestamped {

    @jakarta.persistence.Id
    @GeneratedValue
    private Long Id;

    @Column
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String contents;

    @Column
    private String imageUrl;

    @Column
    private LocalDate date;

    @Column
    private String category;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String newsSummary;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    public Posts(String title, String contents, String imageUrl, LocalDate date, String category, String newsSummary) {

        this.title = title;
        this.contents = contents;
        this.imageUrl = imageUrl;
        this.date = date;
        this.category = category;
        this.newsSummary = newsSummary;

    }

}
