package com.sparta.clonecodingunicorn.domain.posts.repository;

import com.sparta.clonecodingunicorn.domain.posts.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    Page<Posts> findAllByCategory(String category, Pageable pageable);

}
