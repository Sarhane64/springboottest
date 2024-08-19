package org.wildcodeschool.myblog.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleDTO {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long categoryID;
    private String categoryName;
    private List<String> ImageUrls;

    public Long getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryID() {
        return categoryID;
    }

    public Long getCategoryId() {
        return categoryID;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategoryId(Long id) {
        this.categoryID = id;
    }

    public List<String> getImageUrls() { return ImageUrls; }

    public void setImageUrls(List<String> imageUrls) { this.ImageUrls = imageUrls; }
}
