package org.personalblogapi.service;

import java.util.List;

import org.personalblogapi.dto.PaginationDTO;
import org.personalblogapi.model.entity.Blog;

public interface BlogService {

    Blog createBlog(Blog blog);

    Blog updateBlogById(Long id, Blog blog);

    void deleteBlogById(Long id);

    Blog getBlogById(Long id);

    List<Blog> getAllBlogs();

    PaginationDTO<Blog> getAllBlogs(int page, int size);

    void bulk(List<Blog> blogRequests);

}
