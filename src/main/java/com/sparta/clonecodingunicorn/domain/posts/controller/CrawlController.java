package com.sparta.clonecodingunicorn.domain.posts.controller;

import com.sparta.clonecodingunicorn.domain.posts.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crawl")
public class CrawlController {

    private final Scheduler scheduler;

    @Autowired
    public CrawlController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @GetMapping("/run") // 뉴스 크롤링
    public ResponseEntity<String> runCrawling() {
        try {
            scheduler.runCrawling();
            return ResponseEntity.ok("크롤링이 성공적으로 실행되었습니다.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("크롤링 중 오류가 발생했습니다.");
        }
    }
}