package com.erepnikov.controller;

import com.erepnikov.controller.exceptions.ServerErrorException;
import com.erepnikov.controller.util.PaginationUtil;
import com.erepnikov.domain.Article;
import com.erepnikov.service.ArticleService;
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
public class ArticleController {

    private ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/article")
    public ResponseEntity<Void> createArticle(@RequestBody Article article) throws ServerErrorException {
        if (article.getId() != null) {
            throw new ServerErrorException("Article already have an ID");
        }
        this.articleService.save(article);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/article")
    public void updateArticle(@RequestBody Article article) throws ServerErrorException {
        if (article.getId() == null) {
            this.createArticle(article);
        }
        this.articleService.save(article);
    }

    @GetMapping("/article")
    public ResponseEntity<List<Article>> getAllArticles(Pageable pageable) {
        Page<Article> page = this.articleService.getAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/article");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/article/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable Integer id) {
        return new ResponseEntity<>(this.articleService.get(id), HttpStatus.OK);
    }

    @DeleteMapping("/article/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Integer id) {
        this.articleService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
