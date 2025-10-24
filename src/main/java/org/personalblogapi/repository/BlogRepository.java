package org.personalblogapi.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.personalblogapi.model.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    Optional<List<Blog>> findAllBlogByCreatedAtBetween(LocalDate from,
            LocalDate to);

}
