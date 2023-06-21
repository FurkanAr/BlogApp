package com.demo.Blog.repository;

import com.demo.Blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    List<Post> findAllByPublicationDateBetween(LocalDate firstDay, LocalDate lastDay);

    @Query(value = "SElECT * FROM Post WHERE user_id = :userId AND publication_date BETWEEN :firstDay AND :lastDay", nativeQuery = true)
    List<Post> findAllByPublicationDateBetweenByUserId(
            @Param("userId") Long userId,
            @Param("firstDay") LocalDate firstDay,
            @Param("lastDay") LocalDate lastDay);

    @Query(value = "SElECT COUNT(*) FROM Post WHERE user_id = :userId AND publication_date BETWEEN :firstDay AND :lastDay", nativeQuery = true)
    int  CountAllByPublicationDateBetweenByUserId(
            @Param("userId") Long userId,
            @Param("firstDay") LocalDate firstDay,
            @Param("lastDay") LocalDate lastDay);

    List<Post> findAllByUserId(Long userId);


    List<Post> findPostsByTagsId(Long tagId);
}
