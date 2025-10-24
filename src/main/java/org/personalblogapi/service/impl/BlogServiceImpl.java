package org.personalblogapi.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.personalblogapi.dto.PaginationDTO;
import org.personalblogapi.exception.NotFoundException;
import org.personalblogapi.mapper.BlogMapper;
import org.personalblogapi.model.entity.Blog;
import org.personalblogapi.repository.BlogRepository;
import org.personalblogapi.service.BlogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    @Override
    public Blog createBlog(Blog blog) {
        blog.setTitle(blog.getTitle());
        blog.setContent(blog.getContent());
        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlogById(Long id, Blog blog) {
        Blog blogExisted = blogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Blog not found with ID: " + id));
        blogExisted.setTitle(blog.getTitle());
        blogExisted.setContent(blog.getContent());
        blogExisted.setUpdatedAt(LocalDateTime.now());
        return blogRepository.save(blogExisted);
    }

    @Override
    public void deleteBlogById(Long id) {
        blogRepository.findById(id).ifPresentOrElse(blogRepository::delete, () -> {
            throw new NotFoundException("Blog with ID " + id + " not found");
        });
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Blog with ID " + id + " not found"));
    }

    @Override
    public List<Blog> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        if (blogs.isEmpty()) {
            throw new NotFoundException("No blog is found");
        }
        return blogs;

    }

    @Override
    public PaginationDTO<Blog> getAllBlogs(int page, int size) {
        Page<Blog> allBlog = blogRepository.findAll(PageRequest.of(page, size));
        return PaginationDTO.fromPage(allBlog);
    }

    @Transactional
    @Override
    public void bulk(List<Blog> blogRequests) {
        if (blogRequests != null) {
            blogRepository.saveAll(blogRequests);
        }
    }

    @Override
    public List<Blog> getAllBlogsFromDateToDate(LocalDate from, LocalDate to) {
        List<Blog> blogs = blogRepository.findAllBlogByCreatedAtBetween(from, to)
                .orElseThrow(() -> new NotFoundException("No blogs found from: " + from + " to " + to));

        List<Blog> filteredBlogs = blogs.stream()
                .filter(blog -> {
                    LocalDate createdAtDate = blog.getCreatedAt().toLocalDate();
                    return (createdAtDate.isEqual(from) || createdAtDate.isAfter(from)) &&
                            (createdAtDate.isEqual(to) || createdAtDate.isBefore(to));
                })
                .collect(Collectors.toList());
        return filteredBlogs;
    }

}
