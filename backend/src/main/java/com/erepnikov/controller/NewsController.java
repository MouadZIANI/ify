package com.erepnikov.controller;

import com.erepnikov.controller.exceptions.ServerErrorException;
import com.erepnikov.controller.util.PaginationUtil;
import com.erepnikov.domain.News;
import com.erepnikov.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NewsController {

    private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping("/news")
    public ResponseEntity<Void> createNews(@RequestBody News news) throws ServerErrorException {
        if (news.getId() != null) {
            throw new ServerErrorException("News already have an ID");
        }
        this.newsService.save(news);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/news")
    public void updateNews(@RequestBody News news) throws ServerErrorException {
        if (news.getId() == null) {
            this.createNews(news);
        }
        this.newsService.save(news);
    }

    @GetMapping("/news")
    public ResponseEntity<List<News>> getAllNews(Pageable pageable) {
        Page<News> page = this.newsService.getAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/news");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/news/{id}")
    public ResponseEntity<News> getNews(@PathVariable Integer id) {
        return new ResponseEntity<>(this.newsService.get(id), HttpStatus.OK);
    }

    @DeleteMapping("/news/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Integer id) {
        this.newsService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}