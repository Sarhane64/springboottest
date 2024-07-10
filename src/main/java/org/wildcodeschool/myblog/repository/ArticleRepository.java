package org.wildcodeschool.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wildcodeschool.myblog.model.Article;

import java.time.LocalDate;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTitle(String title);

    @Query("SELECT a FROM Article a ORDER BY a.createdAt DESC")
    List<Article> findByOrder();

    @Query(value = "SELECT * FROM article WHERE created_at >=:date ORDER BY created_at DESC", nativeQuery = true)
    List<Article> findByCreatedAtAfter(LocalDate date);

    List<Article> findByTitleContaining(String title);
}