package org.personalblogapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.personalblogapi.dto.BlogRequest;
import org.personalblogapi.dto.PaginationDTO;
import org.personalblogapi.model.entity.Blog;
import org.personalblogapi.model.response.BlogResponse;

@Mapper(componentModel = "spring")
public interface BlogMapper {

    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "blogId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Blog toEntity(BlogRequest blogRequest);

    BlogResponse toResponse(Blog blog);

    List<Blog> toEntity(List<BlogRequest> blogRequests);

    List<BlogResponse> toResponse(List<Blog> blogs);

    default PaginationDTO<BlogResponse> toPaginationResponse(PaginationDTO<Blog> paginationDTO) {
        if (paginationDTO == null) {
            return null;
        }
        return PaginationDTO.<BlogResponse>builder()
                .content(toResponse(paginationDTO.getContent())) // Map the content
                .pageSize(paginationDTO.getPageSize())
                .pageNumber(paginationDTO.getPageNumber())
                .totalPages(paginationDTO.getTotalPages())
                .totalElement(paginationDTO.getTotalElement())
                .numberOfElements(paginationDTO.getNumberOfElements())
                .last(paginationDTO.isLast())
                .first(paginationDTO.isFirst())
                .empty(paginationDTO.isEmpty())
                .build();
    }

}
