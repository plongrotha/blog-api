package org.personalblogapi.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Positive;
import org.personalblogapi.dto.BlogRequest;
import org.personalblogapi.dto.PaginationDTO;
import org.personalblogapi.mapper.BlogMapper;
import org.personalblogapi.model.entity.Blog;
import org.personalblogapi.model.response.ApiResponse;
import org.personalblogapi.model.response.BlogResponse;
import org.personalblogapi.service.BlogService;
import org.personalblogapi.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Blog", description = "APIs for managing blogs")
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogService blogService;
    private final BlogMapper blogMapper;

    @Operation(summary = "Create a new blog", description = "This endpoint allows you to create a new blog post by providing a valid BlogRequest payload.")
    @PostMapping
    public ResponseEntity<ApiResponse<BlogResponse>> createBlog(@Valid @RequestBody BlogRequest blogRequest) {
        Blog saved = blogService.createBlog(blogMapper.toEntity(blogRequest));
        return ResponseUtil.created(blogMapper.toResponse(saved), "Blog is created successfully");
    }

    @Operation(summary = "Update an existing blog", description = "This endpoint updates an existing blog post identified by its ID using the provided BlogRequest payload.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogResponse>> updateBlog(
            @PathVariable Long id,
            @Valid @RequestBody BlogRequest blogRequest) {
        Blog updated = blogService.updateBlogById(id, blogMapper.toEntity(blogRequest));
        return ResponseUtil.ok(blogMapper.toResponse(updated), "Blog is updated successfully");
    }

    @Operation(summary = "Get a blog by ID", description = "Retrieve the details of a specific blog post by providing its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogResponse>> getBlogById(@PathVariable Long id) {
        Blog blog = blogService.getBlogById(id);
        return ResponseUtil.ok(blogMapper.toResponse(blog), "Blog retrieved successfully");
    }

    @Operation(summary = "Delete a blog by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBlogById(@PathVariable @Positive Long id) {
        blogService.deleteBlogById(id);
        return ResponseUtil.ok("Blog deleted successfully");
    }

    @Operation(summary = "Retrieve all blogs")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BlogResponse>>> allBlogs() {
        List<Blog> blogs = blogService.getAllBlogs();
        // List<BlogResponse> response = blogMapper.toResponse(blogs);
        return ResponseUtil.ok(blogMapper.toResponse(blogs), "Retrieve all blogs successfully");
    }

    @Operation(summary = "Retrieve all blogs with pagination")
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<PaginationDTO<BlogResponse>>> AllBlogs(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationDTO<Blog> dto = blogService.getAllBlogs(page, size);
        return ResponseUtil.ok(blogMapper.toPaginationResponse(dto), "Retrieve all blogs successfully");
    }

    @Operation(summary = "Bulk create blogs")
    @PostMapping("/bulks")
    public ResponseEntity<ApiResponse<Void>> bulkCreateBlogs(
            @Valid @RequestBody List<BlogRequest> blogRequests) {
        if (blogRequests == null || blogRequests.isEmpty()) {
            return ResponseUtil.badRequest("Blog list cannot be empty");
        }
        List<Blog> blogs = blogRequests.stream()
                .map(blogMapper::toEntity)
                .toList();
        // Save all blogs
        blogService.bulk(blogs);
        return ResponseUtil.created("Blogs are created successfully");
    }

    @Operation(summary = "Get blogs by date range", description = "Retrieves all blogs published between the specified from and to dates (inclusive)")
    @GetMapping("/date")
    public ResponseEntity<ApiResponse<List<BlogResponse>>> getBlogFromTO(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {
        List<Blog> blogs = blogService.getAllBlogsFromDateToDate(from, to);
        return ResponseUtil.ok(blogMapper.toResponse(blogs), "blogs retrieve successfully");
    }

}
