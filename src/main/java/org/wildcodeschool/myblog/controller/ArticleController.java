package org.wildcodeschool.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.CategoryRepository;
import org.wildcodeschool.myblog.repository.ImageRepository;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        if (((List<?>) articles).isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ArticleDTO> articleDto = articles.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(articleDto);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Article>> getArticlesByTitle(@PathVariable String title) {
        List<Article> articles = articleRepository.findByTitle(title);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/last")
    public ResponseEntity<List<Article>> getLatestArticles() {
        List<Article> articles = articleRepository.findByOrder();
        if(articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Article>> getArticleByDate(@PathVariable LocalDate date) {
        List<Article> articles = articleRepository.findByCreatedAtAfter(date);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Article article = optionalArticle.get();
        return ResponseEntity.ok(convertToDTO(article));
    }


    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        // Ajout de la catégorie
        if (article.getCategory() != null) {
            Category category = categoryRepository.findById(article.getCategory().getId()).orElse(null);
            if (category == null) {
                return ResponseEntity.badRequest().body(null); 
            }
            article.setCategory(category);
        }
        if (article.getImages() != null && !article.getImages().isEmpty()) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : article.getImages()) {
                if (image.getId() != null) {
                    Image existingImage = imageRepository.findById(image.getId()).orElse(null);
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        return ResponseEntity.badRequest().body(null);
                    }
                } else {
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        }
        Article savedArticle = articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedArticle));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Article article = optionalArticle.get();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setUpdatedAt(LocalDateTime.now());

        // Mise à jour de la catégorie
        if (articleDTO.getCategoryId() != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(articleDTO.getCategoryId());
            if (!optionalCategory.isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            Category category = optionalCategory.get();
            article.setCategory(category);
        }
        if (article.getImages() != null) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : article.getImages()) {
                if (image.getId() != null) {
                    Image existingImage = imageRepository.findById(image.getId()).orElse(null);
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        return ResponseEntity.badRequest().build();
                    }
                } else {
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        } else {
            article.getImages().clear();
        }
        Article updatedArticle = articleRepository.save(article);
        return ResponseEntity.ok(convertToDTO(updatedArticle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }
        articleRepository.delete(article);
        return ResponseEntity.noContent().build();
    }

    private ArticleDTO convertToDTO(Article article) {
        ArticleDTO articleDto = new ArticleDTO();
        articleDto.setId(article.getId());
        articleDto.setTitle(article.getTitle());
        articleDto.setContent(article.getContent());
        articleDto.setCreatedAt(article.getCreatedAt());
        articleDto.setUpdatedAt(article.getUpdatedAt());

        if (article.getCategory() != null) {
            articleDto.setCategoryId(article.getCategory().getId());
            articleDto.setCategoryName(article.getCategory().getName());
        }

        if (article.getImages() != null) {
            articleDto.setImageUrls(article.getImages().stream()
                    .map(Image::getUrl)
                    .collect(Collectors.toList()));
        }

        return articleDto;
    }

    private Article convertToEntity(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setId(articleDTO.getId());
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setCreatedAt(articleDTO.getCreatedAt());
        article.setUpdatedAt(articleDTO.getUpdatedAt());
        if (articleDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(articleDTO.getCategoryId()).orElse(null);
            article.setCategory(category);
        }
        return article;
    }
}
