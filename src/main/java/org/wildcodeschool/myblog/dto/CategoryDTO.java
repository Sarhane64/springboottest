package org.wildcodeschool.myblog.dto;

import java.util.List;

public class CategoryDTO {
    private Long id;
    private String name;
    private List<ArticleDTO> articles;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ArticleDTO> getArticles() {
        return articles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setArticles(List<ArticleDTO> articles) {
        this.articles = articles;
    }

    public void setName(String name) {
        this.name = name;
    }
}
